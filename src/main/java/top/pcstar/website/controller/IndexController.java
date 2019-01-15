package top.pcstar.website.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @ClassName IndexController
 * @Description: TODO
 * @Author: panchao
 * @Date: Created in 19-1-11 下午3:20
 * @Version: 1.0
 */
@Controller
public class IndexController {
    @GetMapping("/")
    public String index() {
        return "xiaobudian";
    }

    @GetMapping("/whisper")
    public String whisper() {
        return "whisper";
    }

    @GetMapping("/leacots")
    public String leacots() {
        return "leacots";
    }

    @GetMapping("/album")
    public String album() {
        return "album";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/xiaobudian")
    public String xiaobudian() {
        return "xiaobudian";
    }
}
