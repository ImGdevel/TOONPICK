package toonpick.app.controller;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import toonpick.app.dto.JoinDTO;
import toonpick.app.service.JoinService;

import java.util.logging.Logger;

@Controller
@ResponseBody
public class JoinController {

    private final JoinService joinService;

    public JoinController(JoinService joinService) { this.joinService = joinService; }

    @PostMapping("/join")
    public String joinProcess(JoinDTO joinDTO){
        joinService.createUser(joinDTO);
        return "ok";
    }
}
