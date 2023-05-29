package ute.udn.dodientu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ute.udn.dodientu.dto.MessageDTO;
import ute.udn.dodientu.dto.StatusDTO;
import ute.udn.dodientu.entity.CheckOut;
import ute.udn.dodientu.entity.ReportUserToPayment;
import ute.udn.dodientu.entity.detailCheckOut;
import ute.udn.dodientu.service.CheckOutService;

import java.util.List;

@RestController
@RequestMapping("/api/dodientu/v1/checkout")
public class CheckOutController {
    @Autowired
    private CheckOutService checkOutService;

    @PostMapping
    public ResponseEntity<?> add(@RequestBody CheckOut checkOut) {
        checkOutService.saveOrUpdate(checkOut);
        return ResponseEntity.ok(checkOutService.saveOrUpdate(checkOut));
    }

    @PostMapping("/detail")
    public ResponseEntity<?> addDetail(@RequestBody detailCheckOut detailCheckOut) {
        checkOutService.saveOrUpdateDetail(detailCheckOut);
        return ResponseEntity.ok(new MessageDTO("Lưu thành công!"));
    }

    @GetMapping("/detail")
    public Page<detailCheckOut> findByStatus(@RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                             @RequestParam(name = "limit", required = false, defaultValue = "5") Integer limit,
                                             @RequestParam(name = "sortName", required = false, defaultValue = "DESC") String sortname,
                                             @RequestParam(name = "sortBy", required = false, defaultValue = "id") String sortby,
                                             @RequestParam(name = "status", required = false) int status) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortname), sortby);
        Pageable pageable = PageRequest.of(page - 1, limit, sort);

        return checkOutService.findByStatus(pageable, status);
    }

    @GetMapping
    public List<CheckOut> list() {
        return checkOutService.getAlllist();
    }

    @GetMapping("/{id}")
    public CheckOut findById(@PathVariable("id") Long id) {
        return checkOutService.getOneById(id);
    }

    @GetMapping("/detailId/{id}")
    public detailCheckOut findByIdDetail(@PathVariable("id") Long id) {
        return checkOutService.findByIdDetail(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        checkOutService.delete(id);
    }

    @DeleteMapping("/detail/{id}")
    public void deleteDetail(@PathVariable("id") Long id) {
        checkOutService.deleteDetail(id);
    }

    @PostMapping("/setStatus")
    public ResponseEntity<?> setStatus(@RequestBody StatusDTO dto) {
        checkOutService.saveStatus(dto.getDetailId(), dto.getStatus(), dto.getMessageDestroy());
        return ResponseEntity.ok(new MessageDTO("Lưu thành công!"));
    }

    @GetMapping("/reportUserToPayment")
    public List<ReportUserToPayment> findByReportUserToPayment() {
        return checkOutService.getAllReportUserToPayment();
    }
}
