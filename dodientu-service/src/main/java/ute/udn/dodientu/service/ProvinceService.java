package ute.udn.dodientu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ute.udn.dodientu.dto.CustomDTO;
import ute.udn.dodientu.entity.Province;
import ute.udn.dodientu.exception.ApiException;
import ute.udn.dodientu.exception.NotFoundException;
import ute.udn.dodientu.repository.DistrictRepository;
import ute.udn.dodientu.repository.ProvinceRepository;

import java.util.List;
import java.util.Locale;

@Service
public class ProvinceService {

    @Autowired
    ProvinceRepository provinceRepository;

    @Autowired
    DistrictRepository districtRepository;

    @Autowired
    ResourceBundleMessageSource messageSource;

    public List<Province> findAll() {
        return provinceRepository.findAll();
    }

    public List<Province> findAll(Boolean isDelete) {
        return provinceRepository.findAllByIsDelete(isDelete);
    }

    public Province findById(Long id) {
        return provinceRepository.findById(id).get();
    }

    public Province save(Province entity) {
        Province province = provinceRepository.findByName(entity.getName());

        if (province != null && entity.getId() != null) {
            if (province.getId() != entity.getId())
                throw new ApiException(messageSource.getMessage("ute.udn.validate.name.existing", null, Locale.getDefault()));
            else {
                return provinceRepository.save(entity);
            }
        }

        if (province != null) {
            if (!province.getIsDelete())
                throw new ApiException(messageSource.getMessage("ute.udn.validate.name.existing", null, Locale.getDefault()));
            else {
                province.setIsDelete(true);
                return provinceRepository.save(province);
            }
        }

        return provinceRepository.save(entity);
    }

    public CustomDTO delete(Long id) {
        if (!provinceRepository.existsById(id)) {
            throw new NotFoundException(messageSource.getMessage("ute.udn.validate.id.notfound", null, Locale.getDefault()));
        }

        if (districtRepository.findAllByProvinceIdAndIsDelete(id, false).size() > 0) {
            throw new ApiException(messageSource.getMessage("ute.udn.validate.condition.delete", null, Locale.getDefault()));
        }

        Province province = provinceRepository.findById(id).get();
        province.setIsDelete(true);
        provinceRepository.save(province);

        return new CustomDTO(HttpStatus.OK, messageSource.getMessage("ute.udn.message.success", null, Locale.getDefault()));
    }
}