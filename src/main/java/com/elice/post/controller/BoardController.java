package com.elice.post.controller;

import com.elice.post.dto.BoardDTO;
import com.elice.post.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/save")
    public String saveForm(){
        return "save";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute BoardDTO boardDTO){
        boardService.save(boardDTO);
        return "redirect:/board/paging";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model){
        // 조회수 증가 ++

        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board",boardDTO);
        return "detail";
    }


}
