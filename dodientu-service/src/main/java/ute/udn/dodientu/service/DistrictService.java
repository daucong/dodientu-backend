package ute.udn.dodientu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ute.udn.dodientu.dto.CustomDTO;
import ute.udn.dodientu.entity.District;
import ute.udn.dodientu.entity.Province;
import ute.udn.dodientu.exception.ApiException;
import ute.udn.dodientu.exception.NotFoundException;
import ute.udn.dodientu.repository.DistrictRepository;
import ute.udn.dodientu.repository.ProvinceRepository;
import ute.udn.dodientu.repository.WardRepository;

import java.util.List;
import java.util.Locale;

@Service
public class DistrictService {

    @Autowired
    DistrictRepository districtRepository;

    @Autowired
    WardRepository wardRepository;

    @Autowired
    ProvinceRepository provinceRepository;

    @Autowired
    ResourceBundleMessageSource messageSource;

    public List<District> findAll() {
        return districtRepository.findAll();
    }

    public List<District> findAll(Boolean isDelete) {
        return districtRepository.findAllByIsDelete(isDelete);
    }

    public District findById(Long id) {
        return districtRepository.findById(id).get();
    }

    public District save(District entity) {
        Province province = provinceRepository.findById(entity.getProvince().getId()).get();

        if (province.getIsDelete()) {
            throw new ApiException(messageSource.getMessage("ute.udn.validate.parent.block", null, Locale.getDefault()));
        }

        District district = districtRepository.findByNameAndProvinceId(entity.getName(), entity.getProvince().getId());
        if (district != null && entity.getId() != null) {
            if (district.getId() != entity.getId())
                throw new ApiException(messageSource.getMessage("ute.udn.validate.name.existing", null, Locale.getDefault()));
            else {
                return districtRepository.save(entity);
            }
        }

        if (district != null) {
            if (!district.getIsDelete())
                throw new ApiException(messageSource.getMessage("ute.udn.validate.name.existing", null, Locale.getDefault()));
            else {
                district.setIsDelete(false);
                return districtRepository.save(district);
            }
        }

        return districtRepository.save(entity);
    }

    public CustomDTO delete(Long id) {
        if (!districtRepository.existsById(id)) {
            throw new NotFoundException(messageSource.getMessage("ute.udn.validate.id.notfound", null, Locale.getDefault()));
        }

        if (wardRepository.findAllByDistrictIdAndIsDelete(id, false).size() > 0) {
            throw new ApiException(messageSource.getMessage("ute.udn.validate.condition.delete", null, Locale.getDefault()));
        }

        District district = districtRepository.findById(id).get();
        district.setIsDelete(true);
        districtRepository.save(district);

        return new CustomDTO(HttpStatus.OK, messageSource.getMessage("ute.udn.message.success", null, Locale.getDefault()));
    }

    public List<District> findAllByProvinceID(Long id) {
        return districtRepository.findAllByProvinceIdAndIsDelete(id, false);
    }
}