package com.highgeupsik.backend.api;


import com.highgeupsik.backend.dto.MessageReqDTO;
import com.highgeupsik.backend.dto.MessageResDTO;
import com.highgeupsik.backend.dto.RoomResDTO;
import com.highgeupsik.backend.entity.Message;
import com.highgeupsik.backend.entity.User;
import com.highgeupsik.backend.service.MessageQueryService;
import com.highgeupsik.backend.service.MessageService;
import com.highgeupsik.backend.service.RoomQueryService;
import com.highgeupsik.backend.service.RoomService;
import com.highgeupsik.backend.utils.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import static com.highgeupsik.backend.utils.ApiUtils.*;

@RestController
@RequiredArgsConstructor
public class MessageApiController {

    private final RoomService roomService;
    private final RoomQueryService roomQueryService;
    private final MessageService messageService;
    private final MessageQueryService messageQueryService;

    @GetMapping("/messages/{id}") //메세지 조회
    public ApiResult<MessageResDTO> seeOneMessage(@PathVariable("id") Long messageId) {
        Message message = messageQueryService.findOneById(messageId);
        return success(new MessageResDTO(message));
    }

    @PostMapping("/messages") //너무길어서 생각다시해보기
    public ApiResult sendMessage(@RequestBody MessageReqDTO messageReqDTO,
                                 @AuthenticationPrincipal User user) {
        Long fromRoomId;
        Long toRoomId;
        Long fromUserId = user.getId();
        Long toUserId = messageReqDTO.getToUserId();
        String message = messageReqDTO.getContent();
        RoomResDTO fromRoomResDTO = roomQueryService.findOneByUserId(fromUserId, toUserId);
        RoomResDTO toRoomResDTO = roomQueryService.findOneByUserId(toUserId, fromUserId);
        if (fromRoomResDTO == null && toRoomResDTO == null) { //처음 쪽지 보낼때
            fromRoomId = roomService.saveRoom(fromUserId, toUserId);
            toRoomId = roomService.saveRoom(toUserId, fromUserId);
        } else if (fromRoomResDTO != null && toRoomResDTO == null) { //내가 삭제안하고 상대방은 삭제했을때
            fromRoomId = roomQueryService.findOneByUserId(fromUserId, toUserId).getId();
            toRoomId = roomService.saveRoom(toUserId, fromUserId);
        } else if (fromRoomResDTO == null && toRoomResDTO != null) { //내가 삭제하고 상대방은 삭제안했을때
            fromRoomId = roomService.saveRoom(fromUserId, toUserId);
            toRoomId = roomQueryService.findOneByUserId(fromUserId, toUserId).getId();
        } else { //둘다 방을 유지하고 있을때
            fromRoomId = roomQueryService.findOneByUserId(fromUserId, toUserId).getId();
            toRoomId = roomQueryService.findOneByUserId(fromUserId, toUserId).getId();
        }
        messageService.saveMessage(message, fromUserId, toUserId, fromRoomId);
        messageService.saveMessage(message, fromUserId, toUserId, toRoomId);
        roomService.updateMessage(fromRoomId, message);
        roomService.updateMessage(toRoomId, message);
        return success(message);
    }

    @DeleteMapping("/messages/{messageId}") //메세지 삭제 ?? 필요한가
    public ApiResult deleteMessage(@PathVariable("messageId") Long messageId,
                                   @AuthenticationPrincipal User user) {
        messageService.deleteMessage(messageId, user.getId());
        return success(null);
    }

    @GetMapping("/rooms") //쪽지 대화방 목록 조회
    public ApiResult<List<RoomResDTO>> rooms(@RequestParam(value = "page", defaultValue = "1") Integer pageNum,
                                             @AuthenticationPrincipal User user) {
        return success(roomQueryService.findRooms(user.getId(), pageNum));
    }

    @GetMapping("/rooms/{roomId}") //쪽지 대화방 조회
    public ApiResult<List<MessageResDTO>> room(@RequestParam(value = "page", defaultValue = "1") Integer pageNum,
                                               @PathVariable("roomId") Long roomId,
                                               @AuthenticationPrincipal User user) {
        return success(messageQueryService.findMessages(roomId, pageNum));
    }

    @DeleteMapping("/rooms/{roomId}") //대화방 삭제
    public ApiResult deleteRoom(@PathVariable("roomId") Long roomId,
                                @AuthenticationPrincipal User user) {
        roomService.deleteRoom(roomId);
        return success(null);
    }

}
