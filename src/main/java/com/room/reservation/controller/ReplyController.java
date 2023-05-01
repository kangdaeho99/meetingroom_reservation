package com.room.reservation.controller;


import com.room.reservation.dto.ReplyDTO;
import com.room.reservation.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/replies/")
@Log4j2
@RequiredArgsConstructor
public class ReplyController {
    private final ReplyService replyService;

    @GetMapping(value = "/room/{rno}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReplyDTO>> getListByBoard(@PathVariable("rno") Long rno){
        log.info("rno : "+rno);
        log.info("replygetList: "+replyService.getList(rno));
        return new ResponseEntity<>( replyService.getList(rno), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Long> register(@RequestBody ReplyDTO replyDTO){
        log.info(replyDTO);
        Long rno = replyService.register(replyDTO);
        return new ResponseEntity<>(rno, HttpStatus.OK);
    }

    @DeleteMapping("/{replyno}")
    public ResponseEntity<String> remove(@PathVariable("replyno") Long replyno){
        log.info("replyno: "+replyno);
        replyService.remove(replyno);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @PutMapping("/{replyno}")
    public ResponseEntity<String> modify(@RequestBody ReplyDTO replyDTO){
        log.info(replyDTO);
        replyService.modify(replyDTO);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
//    @PutMapping("/{rno}")
//    public
}
