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

    private final RoomService service; //final 로 선언

//    @GetMapping({"","/","/list"})
//    @GetMapping({"","/"})
//    public String index(){
//        log.info("list.............");
////        return "room/list";
//        return "redirect:/room/list";
//    }

    @GetMapping({"","/","/list"})
    public void list(PageRequestDTO pageRequestDTO, Model model){
        log.info("list..............." + pageRequestDTO);
        model.addAttribute("result", service.getList(pageRequestDTO));
        System.out.println("result:::::::::::::::::"+ model.getAttribute("result"));
    }

    @GetMapping("/register")
    public void register(){
        log.info("register get ...");
    }

    @PostMapping("/register")
    public String registerPost(RoomDTO dto, RedirectAttributes redirectAttributes){
        log.info("dto..."+ dto);

        //새로 추가된 엔티티의 번호
        Long gno = service.register(dto);
        redirectAttributes.addFlashAttribute("msg", gno);
        return "redirect:/room/list";
    }

    @GetMapping("/read")
    public void read(long gno, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model){
        log.info("gno: "+gno);
        RoomDTO dto = service.read(gno);
        model.addAttribute("dto", dto);

    }


}
