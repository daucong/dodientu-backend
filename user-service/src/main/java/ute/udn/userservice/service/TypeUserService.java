package com.sdt.userservice.service;

import com.sdt.userservice.entity.TypeUser;
import com.sdt.userservice.entity.User;
import com.sdt.userservice.repository.TypeUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeUserService {
    @Autowired
    private TypeUserRepository typeUserRepository;

    public List<TypeUser> getAlllist() {
        return typeUserRepository.findAll();
    }

    public TypeUser getOneById(Long id) {
        return typeUserRepository.findById(id).get();
    }

    public TypeUser saveOrUpdate(TypeUser entity) {
        return typeUserRepository.save(entity);
    }

    public boolean delete(Long id) {
        List<User> typeUsers = typeUserRepository.findAllById(id);
        if (typeUsers.size() > 0) {
            try {
                throw new Exception("Bạn phải xóa hết các user liên quan đến danh mục trước!!!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        typeUserRepository.deleteById(id);
        return false;
    }
}
