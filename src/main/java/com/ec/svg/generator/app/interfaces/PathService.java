package com.ec.svg.generator.app.interfaces;

import com.ec.svg.generator.app.dto.PathDTO;

import java.util.List;

public interface PathService {

    List<PathDTO> getByUnicode(Integer unicode);
}
