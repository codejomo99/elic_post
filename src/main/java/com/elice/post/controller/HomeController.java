package com.elice.post.controller;

import com.elice.post.dto.BoardDTO;
import com.elice.post.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class HomeController {

    private final BoardService boardService;

    @GetMapping("/")
    public String paging(@PageableDefault(page = 1) Pageable pageable, Model model){
//        pageable.getPageNumber();
        return "paging";
    }

}
