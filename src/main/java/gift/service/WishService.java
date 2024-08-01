package gift.service;

import gift.dto.WishResponse;
import gift.model.Wish;
import gift.model.Member;
import gift.model.Product;
import gift.model.ProductOption;
import gift.repository.WishRepository;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.ProductOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishService {

    private final WishRepository wishRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;

    @Autowired
    public WishService(WishRepository wishRepository, MemberRepository memberRepository, ProductRepository productRepository, ProductOptionRepository productOptionRepository) {
        this.wishRepository = wishRepository;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
        this.productOptionRepository = productOptionRepository;
    }

    public Page<WishResponse> getWishesByMemberId(Long memberId, Pageable pageable) {
        Page<Wish> wishes = wishRepository.findByMemberId(memberId, pageable);
        return wishes.map(this::convertToWishResponse);
    }

    public Wish addWish(Long memberId, Long productId, Long optionId, int quantity) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Member not found"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found"));
        ProductOption productOption = productOptionRepository.findById(optionId).orElseThrow(() -> new IllegalArgumentException("Product option not found"));

        Wish wish = new Wish();
        wish.setMember(member);
        wish.setProduct(product);
        wish.setProductOption(productOption);
        wish.setQuantity(quantity);
        return wishRepository.save(wish);
    }

    @Transactional
    public Wish updateWish(Long wishId, Long productId, Long optionId, int quantity) {
        Wish wish = wishRepository.findById(wishId).orElseThrow(() -> new IllegalArgumentException("Wish not found"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found"));
        ProductOption productOption = productOptionRepository.findById(optionId).orElseThrow(() -> new IllegalArgumentException("Product option not found"));

        wish.setProduct(product);
        wish.setProductOption(productOption);
        wish.setQuantity(quantity);
        return wish;
    }

    public void deleteWish(Long wishId) {
        if (!wishRepository.existsById(wishId)) {
            throw new IllegalArgumentException("Wish not found with id: " + wishId);
        }
        wishRepository.deleteById(wishId);
    }

    private WishResponse convertToWishResponse(Wish wish) {
        WishResponse wishResponse = new WishResponse();
        wishResponse.setId(wish.getId());
        wishResponse.setProductId(wish.getProduct().getId());
        wishResponse.setOptionId(wish.getProductOption().getId());
        wishResponse.setQuantity(wish.getQuantity());
        return wishResponse;
    }

    public void deleteWishByProductOptionIdAndMemberId(Long optionId, Long memberId) {
        Wish wish = wishRepository.findByProductOptionIdAndMemberId(optionId, memberId);
        if (wish != null) {
            wishRepository.delete(wish);
        }
    }
}
