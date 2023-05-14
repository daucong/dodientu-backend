package ute.udn.dodientu.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import ute.udn.dodientu.entity.detailCheckOut;

public interface CheckOutDetailRepository extends PagingAndSortingRepository<detailCheckOut, Long> {
    Page<detailCheckOut> findByStatus(Pageable pageable, int status);
}
