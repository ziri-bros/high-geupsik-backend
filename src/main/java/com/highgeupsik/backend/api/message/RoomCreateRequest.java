package com.highgeupsik.backend.api.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoomCreateRequest {
	private Long boardId;
	private Long receiverId;
}
