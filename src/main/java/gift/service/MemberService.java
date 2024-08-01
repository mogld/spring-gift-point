package gift.service;

import gift.dto.MemberRequest;
import gift.dto.MemberResponse;
import gift.model.Member;
import gift.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public MemberService(MemberRepository memberRepository, BCryptPasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public MemberResponse register(MemberRequest memberRequest) {
        if (memberRepository.findByEmail(memberRequest.getEmail()) != null) {
            throw new IllegalArgumentException("Email already exists");
        }
        String encodedPassword = passwordEncoder.encode(memberRequest.getPassword());
        Member member = new Member();
        member.setEmail(memberRequest.getEmail());
        member.setPassword(encodedPassword);
        Member savedMember = memberRepository.save(member);
        return new MemberResponse(savedMember);
    }

    @Transactional(readOnly = true)
    public MemberResponse authenticate(String email, String password) {
        Member member = memberRepository.findByEmail(email);
        if (member != null && passwordEncoder.matches(password, member.getPassword())) {
            return new MemberResponse(member);
        }
        return null;
    }

    @Transactional(readOnly = true)
    public MemberResponse findByEmail(String email) {
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new IllegalArgumentException("Member not found");
        }
        return new MemberResponse(member);
    }

    @Transactional(readOnly = true)
    public MemberResponse findById(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Member not found"));
        return new MemberResponse(member);
    }
}
