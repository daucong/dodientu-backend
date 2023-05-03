package ute.udn.dodientu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ute.udn.dodientu.dto.CustomDTO;
import ute.udn.dodientu.entity.District;
import ute.udn.dodientu.entity.Ward;
import ute.udn.dodientu.exception.ApiException;
import ute.udn.dodientu.exception.NotFoundException;
import ute.udn.dodientu.repository.DistrictRepository;
import ute.udn.dodientu.repository.PostRepository;
import ute.udn.dodientu.repository.WardRepository;

import java.util.List;
import java.util.Locale;

@Service
public class WardService {

    @Autowired
    WardRepository wardRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    DistrictRepository districtRepository;

    @Autowired
    ResourceBundleMessageSource messageSource;

    public List<Ward> findAll() {
        return wardRepository.findAll();
    }

    public List<Ward> findAll(Boolean isDelete) {
        return wardRepository.findAllByIsDelete(isDelete);
    }

    public Ward findById(Long id) {
        return wardRepository.findById(id).get();
    }

    public Ward save(Ward entity) {
        District district = districtRepository.findById(entity.getDistrict().getId()).get();

        if (district.getIsDelete()) {
            throw new ApiException(messageSource.getMessage("ute.udn.validate.parent.block", null, Locale.getDefault()));
        }

        Ward ward = wardRepository.findByNameAndDistrictId(entity.getName(), entity.getDistrict().getId());

        if (ward != null && entity.getId() != null) {
            if (ward.getId() != entity.getId())
                throw new ApiException(messageSource.getMessage("ute.udn.validate.name.existing", null, Locale.getDefault()));
            else {
                return wardRepository.save(entity);
            }
        }

        if (ward != null) {
            if (!ward.getIsDelete())
                throw new ApiException(messageSource.getMessage("ute.udn.validate.name.existing", null, Locale.getDefault()));
            else {
                ward.setIsDelete(false);
                return wardRepository.save(ward);
            }
        }

        return wardRepository.save(entity);
    }

    public CustomDTO delete(Long id) {
        if (!wardRepository.existsById(id)) {
            throw new NotFoundException(messageSource.getMessage("ute.udn.validate.id.notfound", null, Locale.getDefault()));
        }

        if (postRepository.findAllByWardIdAndIsDelete(id, false).size() > 0) {
            throw new ApiException(messageSource.getMessage("ute.udn.validate.condition.delete", null, Locale.getDefault()));
        }

        Ward ward = wardRepository.findById(id).get();
        ward.setIsDelete(true);
        wardRepository.save(ward);

        return new CustomDTO(HttpStatus.OK, messageSource.getMessage("ute.udn.message.success", null, Locale.getDefault()));
    }

    public List<Ward> findAllByDistrictID(Long id) {
        return wardRepository.findAllByDistrictIdAndIsDelete(id, false);
    }
}