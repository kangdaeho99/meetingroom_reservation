package com.room.reservation.controller;

import com.room.reservation.dto.PageRequestDTO;
import com.room.reservation.dto.RoomDTO;
import com.room.reservation.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/room")
@Log4j2
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

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
        Long rno = roomService.register(dto);
        redirectAttributes.addFlashAttribute("msg", rno);
        return "redirect:/room/list";
    }

    @GetMapping({"/read", "/modify"})
    public void read(long rno, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model){
        log.info("rno: "+rno);
        RoomDTO dto = roomService.read(rno);
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
