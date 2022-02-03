package com.highgeupsik.backend.service;

import static java.util.stream.Collectors.toList;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.highgeupsik.backend.dto.UploadFileDTO;
import com.highgeupsik.backend.exception.FileUploadException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class S3Service {

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	private final AmazonS3 s3Client;

	public List<UploadFileDTO> uploadFiles(List<MultipartFile> images) {
		return images.stream()
			.map(file -> {
				String fileKey = generateRandomFileKey(file);
				String fileUrl = uploadToS3(fileKey, file);
				return new UploadFileDTO(file.getOriginalFilename(), fileUrl);
			}).collect(toList());
	}

	private String generateRandomFileKey(MultipartFile file) {
		return UUID.randomUUID() + file.getOriginalFilename();
	}

	private String uploadToS3(String fileKey, MultipartFile file) {
		s3Client.putObject(
			new PutObjectRequest(
				bucket,
				fileKey,
				getBytesInputStream(file),
				getMetadata(file)
			).withCannedAcl(CannedAccessControlList.PublicRead)
		);
		return s3Client.getUrl(bucket, fileKey).toString();
	}

	private InputStream getBytesInputStream(MultipartFile file) {
		try {
			return new ByteArrayInputStream(file.getBytes());
		} catch (IOException e) {
			throw new FileUploadException("파일 업로드에 실패했습니다.");
		}
	}

	private ObjectMetadata getMetadata(MultipartFile file) {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(file.getSize());
		metadata.setContentType(file.getContentType());
		return metadata;
	}
}