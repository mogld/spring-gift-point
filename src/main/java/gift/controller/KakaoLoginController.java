package gift.controller;

import gift.auth.JwtTokenUtil;
import gift.dto.DomainResponse;
import gift.model.HttpResult;
import gift.model.Member;
import gift.service.KakaoAuthService;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@Tag(name = "Kakao API", description = "APIs related to Kakao login operations")
public class KakaoLoginController {

    @Autowired
    private KakaoAuthService kakaoAuthService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping("/kakao/callback")
    @Operation(summary = "카카오 로그인 콜백", description = "카카오 로그인 후 리디렉션된 콜백을 처리")
    public ResponseEntity<DomainResponse> kakaoCallback(@RequestParam String code) {
        try {
            // 카카오 액세스 토큰을 가져옵니다.
            String accessToken = kakaoAuthService.getAccessToken(code);

            // 액세스 토큰을 사용하여 사용자 이메일을 가져옵니다.
            String email = kakaoAuthService.getUserEmail(accessToken);
            Member member = memberService.findByEmail(email);

            // 이메일로 회원을 찾거나 새로 생성합니다.
            if (member == null) {
                member = memberService.register(email,"",""); // 비밀번호는 빈 문자열로 설정
            }

            // JWT 토큰을 생성합니다.
            String jwtToken = jwtTokenUtil.generateToken(member);

            // 응답을 생성합니다.
            HttpResult httpResult = new HttpResult(HttpStatus.OK.value(), "Login successful");
            return new ResponseEntity<>(new DomainResponse(httpResult, List.of(Map.of("token", jwtToken)), HttpStatus.OK), HttpStatus.OK);
        } catch (Exception e) {
            HttpResult httpResult = new HttpResult(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(new DomainResponse(httpResult, List.of(Map.of("error", e.getMessage())), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }
}