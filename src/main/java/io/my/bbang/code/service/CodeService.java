package io.my.bbang.code.service;

import org.springframework.stereotype.Service;

import io.my.bbang.code.domain.Code;
import io.my.bbang.code.repository.CodeRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class CodeService {
    private final CodeRepository codeRepository;

    public Flux<Code> findAllByParentCode(String parentCode) {
        return codeRepository.findAllByParentCode(parentCode);
    }
    
}
