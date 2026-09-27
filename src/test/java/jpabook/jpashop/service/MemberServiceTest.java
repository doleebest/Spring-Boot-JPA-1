package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // 기본이 롤백
class MemberServiceTest {
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;
    @Test
    public void 회원가입() throws Exception{
        // given
        Member member = new Member();
        member.setName("kim");

        // when
        Long savedId = memberService.join(member);

        // then
        em.flush();
        assertEquals(member, memberRepository.findOne(savedId));

        // pk 값이 같으면 같은 영속성 컨텍스트 내에서 똑같은 것으로 관리가 됨
    }

    @Test
    public void 중복_회원_예외() throws Exception{
        // given
        Member member = new Member();
        member.setName("kim1");

        Member member2 = new Member();
        member.setName("kim1");

        // when
        memberService.join(member);
        try{
        memberService.join(member2); // 예외 발생해야 한다.
        }catch (IllegalStateException e){
            return; // 테스트 케이스 성공 == 정상
        }

        // then
        fail("예외가 발생해야 한다."); // 여기까지 오면 테스트 잘못 작성한 것.
//        assertThrows(IllegalStateException.class, () -> memberService.join(member2));
    }

}