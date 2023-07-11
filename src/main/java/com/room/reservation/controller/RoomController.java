package com.room.reservation.controller;

import com.room.reservation.dto.PageRequestDTO;
import com.room.reservation.dto.RoomDTO;
import com.room.reservation.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequestMapping("/room/")
@Log4j2
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;


    @PreAuthorize("permitAll()")
    @PostMapping(value= "")
    public ResponseEntity<Long> register(@ModelAttribute RoomDTO roomDTO){
        log.info("-------------------register-------------------");
        log.info(roomDTO);
        Long rno = roomService.registerWithRoomImage(roomDTO);
        return new ResponseEntity<>(rno, HttpStatus.OK);
    }

    @PreAuthorize("permitAll()")
    @GetMapping(value="/{rno}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoomDTO> read(@PathVariable("rno") long rno){
        log.info("---------------read------------------");
        log.info(rno);
        return new ResponseEntity<>(roomService.get(rno), HttpStatus.OK);
    }

    @DeleteMapping(value="/{rno}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> remove(@PathVariable("rno") Long rno){
        log.info("-----------------remove-------------------");
        log.info(rno);
        roomService.remove(rno);
        return new ResponseEntity<>("removed", HttpStatus.OK);
    }

    @PutMapping(value="/{rno}", produces=MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> modify(@RequestBody RoomDTO roomDTO){
        log.info("---------------modify------------------");
        log.info(roomDTO);
        roomService.modify(roomDTO);
        return new ResponseEntity<>("modified", HttpStatus.OK);
    }


















    @GetMapping({"/","/list"})
    public void list(PageRequestDTO pageRequestDTO, Model model){
        log.info("list ................." + pageRequestDTO);
        model.addAttribute("result", roomService.getList(pageRequestDTO));
    }

    @GetMapping("/register")
    public void register(){
        log.info("register get ...");
    }

    @PostMapping("/register")
    public String registerPost(RoomDTO dto, RedirectAttributes redirectAttributes){
        log.info("rno ... "+ dto);
        //새로 추가된 엔티티의 번호
        Long rno = roomService.registerWithRoomImage(dto);
        redirectAttributes.addFlashAttribute("msg", rno);
        return "redirect:/room/list";
    }

    @GetMapping({"/read", "/modify"})
    public void read(long rno, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model){
        log.info("rno: "+rno);
        RoomDTO dto = roomService.getRoomWithMemberImageReview(rno);
        log.info("DTO: "+dto);
        model.addAttribute("dto", dto);
    }

    @PostMapping("/remove")
    public String remove(long rno, RedirectAttributes redirectAttributes){
        log.info("rno: "+rno);
        roomService.remove(rno);
        redirectAttributes.addFlashAttribute("msg",rno);
        return "redirect:/room/list";
    }

    @PostMapping("/modify")
    public String modify(RoomDTO dto, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, RedirectAttributes redirectAttributes){
        log.info("post modify ...........");
        log.info("dto: " + dto);

        roomService.modify(dto);

        redirectAttributes.addAttribute("page", requestDTO.getPage());
        redirectAttributes.addAttribute("type", requestDTO.getType());
        redirectAttributes.addAttribute("keyword", requestDTO.getKeyword());
        redirectAttributes.addAttribute("rno", dto.getRno());

        return "redirect:/room/read";
    }


}
