package com.highgeupsik.backend.api;

import static com.highgeupsik.backend.utils.ApiUtils.success;
import static org.springframework.http.HttpStatus.*;

import com.highgeupsik.backend.dto.MessageReqDTO;
import com.highgeupsik.backend.dto.MessageResDTO;
import com.highgeupsik.backend.dto.RoomDTO;
import com.highgeupsik.backend.resolver.LoginUser;
import com.highgeupsik.backend.service.MessageQueryService;
import com.highgeupsik.backend.service.RoomMessageService;
import com.highgeupsik.backend.service.RoomQueryService;
import com.highgeupsik.backend.utils.ApiResult;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/rooms")
@RestController
public class RoomMessageController {

    private final RoomMessageService roomMessageService;
    private final RoomQueryService roomQueryService;
    private final MessageQueryService messageQueryService;

    @ApiOperation(value = "메세지룸 목록 조회")
    @GetMapping()
    public ApiResult<Page<RoomDTO>> rooms(@LoginUser Long userId,
        @RequestParam(value = "page", defaultValue = "1") Integer pageNum) {
        return success(roomQueryService.findAll(userId, pageNum));
    }

    @ApiOperation(value = "메세지 목록 조회")
    @GetMapping("/{roomId}")
    public ApiResult<Page<MessageResDTO>> messages(@PathVariable Long roomId
        , @RequestParam(value = "page", defaultValue = "1") Integer pageNum) {
        return success(messageQueryService.findAll(roomId, pageNum));
    }

    @ApiOperation(value = "메세지룸 삭제")
    @DeleteMapping("/{roomId}")
    public void roomRemove(@PathVariable Long roomId) {
        roomMessageService.removeRoom(roomId);
    }

    @ApiOperation(value = "메세지 전송")
    @ResponseStatus(CREATED)
    @PostMapping("/messages")
    public void messageSave(@LoginUser Long userId, @RequestBody MessageReqDTO messageReqDTO) {
        roomMessageService.sendMessage(userId, messageReqDTO.getToUserId(), messageReqDTO.getContent());
    }
}
