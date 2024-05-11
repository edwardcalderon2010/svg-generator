package com.ec.svg.generator.app.service;

import com.ec.svg.generator.app.dto.PathDTO;
import com.ec.svg.generator.app.interfaces.PathService;
import com.ec.svg.generator.app.model.entity.Path;
import com.ec.svg.generator.app.model.entity.PathRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PathServiceImpl implements PathService {

    private PathRepository pathRepository;
    private ModelMapper modelMapper;

    public PathServiceImpl(PathRepository pathRepository, ModelMapper modelMapper) {
        this.pathRepository = pathRepository;
        this.modelMapper = modelMapper;
    }
    public List<PathDTO> getByUnicode(Integer unicode) {
        List<Path> fetchedPaths = pathRepository.findByUnicode(unicode);
        List<PathDTO> returnedDTOs = new ArrayList<>();

        if (fetchedPaths != null && fetchedPaths.size() > 0) {
            fetchedPaths.forEach(elem -> returnedDTOs.add(modelMapper.map(elem, PathDTO.class)));
        }
        return returnedDTOs;
    }
}
