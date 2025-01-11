package toonpick.app.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import toonpick.app.auth.dto.JoinRequest;
import toonpick.app.auth.service.JoinService;

@Controller
@ResponseBody
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;

    @PostMapping("/join")
    public ResponseEntity<?> joinProcess(@RequestBody JoinRequest joinRequest) {
        joinService.registerNewMember(joinRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
