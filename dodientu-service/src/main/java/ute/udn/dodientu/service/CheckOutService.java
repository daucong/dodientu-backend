package ute.udn.dodientu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ute.udn.dodientu.entity.CheckOut;
import ute.udn.dodientu.entity.ReportUserToPayment;
import ute.udn.dodientu.entity.detailCheckOut;
import ute.udn.dodientu.repository.CheckOutDetailRepository;
import ute.udn.dodientu.repository.CheckOutRepository;
import ute.udn.dodientu.repository.ReportUserToPaymentRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class CheckOutService {
    @Autowired
    private CheckOutRepository checkOutRepository;
    @Autowired
    private CheckOutDetailRepository checkOutDetailRepository;
    @Autowired
    private ReportUserToPaymentRepository reportUserToPaymentRepository;

    public CheckOut getOneById(Long id) {
        return checkOutRepository.findById(id).get();
    }

    public detailCheckOut findByIdDetail(Long id) {
        return checkOutDetailRepository.findById(id).get();
    }

    public Page<detailCheckOut> findByStatus(Pageable pageable, int status) {
        return checkOutDetailRepository.findByStatus(pageable, status);
    }

    public List<CheckOut> getAlllist() {
        return checkOutRepository.findAll();
    }

    public CheckOut saveOrUpdate(CheckOut checkOut) {
        return checkOutRepository.save(checkOut);
    }

    public detailCheckOut saveOrUpdateDetail(detailCheckOut detailCheckOut) {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        ReportUserToPayment reportUserToPayment = new ReportUserToPayment();
        reportUserToPayment.setCreateDate(formatter.format(date));
        reportUserToPayment.setTotalMoney(detailCheckOut.getTotalPrice());
        reportUserToPayment.setTotalQuality(detailCheckOut.getQualityPay());
        reportUserToPayment.setUserId(detailCheckOut.getUserId());
        reportUserToPayment.setPost(detailCheckOut.getPostId());
        reportUserToPaymentRepository.save(reportUserToPayment);

        return checkOutDetailRepository.save(detailCheckOut);
    }

    public List<ReportUserToPayment> getAllReportUserToPayment() {
        return reportUserToPaymentRepository.findAll();
    }

    public detailCheckOut saveStatus(Long id, int status, String messageDestroy) {
        detailCheckOut detailCheckOut = checkOutDetailRepository.findById(id).get();

        detailCheckOut.setStatus(status);
        detailCheckOut.setMessageDestroy(messageDestroy);
        return checkOutDetailRepository.save(detailCheckOut);
    }

    public boolean delete(Long id) {
        checkOutRepository.deleteById(id);
        return false;
    }

    public boolean deleteDetail(Long id) {
        checkOutDetailRepository.deleteById(id);
        return false;
    }
}