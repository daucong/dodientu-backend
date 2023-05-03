package ute.udn.dodientu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ute.udn.dodientu.dto.CustomDTO;
import ute.udn.dodientu.entity.TypePost;
import ute.udn.dodientu.exception.ApiException;
import ute.udn.dodientu.exception.NotFoundException;
import ute.udn.dodientu.repository.PostRepository;
import ute.udn.dodientu.repository.TypePostRepository;

import java.util.List;
import java.util.Locale;

@Service
public class TypePostService {

    @Autowired
    TypePostRepository typePostRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    ResourceBundleMessageSource messageSource;

    public List<TypePost> findAll() {
        return typePostRepository.findAll();
    }

    public List<TypePost> findAll(Boolean isDelete) {
        return typePostRepository.findAllByIsDelete(isDelete);
    }

    public TypePost findById(Long id) {
        return typePostRepository.findById(id).get();
    }

    public TypePost save(TypePost entity) {
        TypePost typePost = typePostRepository.findByTypePostName(entity.getTypePostName());

        if (typePost != null && entity.getId() != null) {
            if (typePost.getId() != entity.getId())
                throw new ApiException(messageSource.getMessage("ute.udn.validate.name.existing", null, Locale.getDefault()));
            else {
                return typePostRepository.save(entity);
            }
        }

        if (typePost != null) {
            if (!typePost.getIsDelete())
                throw new ApiException(messageSource.getMessage("ute.udn.validate.name.existing", null, Locale.getDefault()));
            else {
                typePost.setIsDelete(false);
                return typePostRepository.save(typePost);
            }
        }

        return typePostRepository.save(entity);
    }

    public CustomDTO delete(Long id) {
        if (!typePostRepository.existsById(id)) {
            throw new NotFoundException(messageSource.getMessage("ute.udn.validate.id.notfound", null, Locale.getDefault()));
        }

        if (postRepository.findAllByTypePostIdAndIsDelete(id, false).size() > 0) {
            throw new ApiException(messageSource.getMessage("ute.udn.validate.condition.delete", null, Locale.getDefault()));
        }

        TypePost typePost = typePostRepository.findById(id).get();
        typePost.setIsDelete(true);
        typePostRepository.save(typePost);

        return new CustomDTO(HttpStatus.OK, messageSource.getMessage("ute.udn.message.success", null, Locale.getDefault()));
    }
}