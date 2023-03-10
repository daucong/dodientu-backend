package ute.udn.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ute.udn.userservice.entity.Bank;
import ute.udn.userservice.service.BankService;

import java.util.List;

@RestController
@RequestMapping("/api/user/v1/bank")
public class BankController {
    @Autowired
    private BankService bankService;

    @GetMapping
    public List<Bank> list() {
        return bankService.getAlllist();
    }

    @PostMapping
    public void addBank(@RequestBody Bank bank) {
        bankService.saveOrUpdate(bank);
    }

    @GetMapping("/{id}")
    public Bank findBankId(@PathVariable("id") Long id) {
        return bankService.getOneById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteBank(@PathVariable("id") Long id) {
        bankService.delete(id);
    }
}
