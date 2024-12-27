package toonpick.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import toonpick.app.dto.JoinRequestDTO;
import toonpick.app.service.JoinService;

@Controller
@ResponseBody
public class JoinController {

    private final JoinService joinService;

    public JoinController(JoinService joinService) {
        this.joinService = joinService;
    }

    @PostMapping("/join")
    public ResponseEntity<String> joinProcess(@RequestBody JoinRequestDTO joinRequestDTO) {
        joinService.createMember(joinRequestDTO);
        return ResponseEntity.ok("Member created successfully.");
    }
}
