package com.univercity.unlimited.greenUniverCity.util;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseApplyMapper<TCreateDTO, TUpdateDTO, TEntity> {
    /// -------------------------------------------
    ///         기본적으로 들어가야하는 값입니다.
    ///         생성자로 이름을 정의하게 됩니다.
    /// -------------------------------------------
    private final String baseName;

    protected BaseApplyMapper(String baseName) {
        this.baseName = baseName;
    }

    /// -------------------------------------------
    ///         DTO -> Entity로 변환을 진행합니다.
    ///         검증 로직이 포함됩니다.
    ///         final은 재정의가 불가능 합니다.
    /// -------------------------------------------
    // TCreateDTO -> TEntity
    public final TEntity applyCreate(TCreateDTO dto) {
        validateCreate(dto);
        return createEntity(dto);
    }

    // TUpdateDTO -> TEntity
    public final TEntity applyUpdate(TUpdateDTO dto, TEntity entity) {
        validateUpdate(dto);
        validateEntity(entity);
        return updateEntity(dto, entity);
    }

    /// -------------------------------------------
    ///         유효한 데이터인지 검증합니다.
    ///         가볍게 체크하게 됩니다.
    ///         final은 재정의가 불가능 합니다.
    /// -------------------------------------------
    protected void validateEntity(TEntity entity) {
        log.info(baseName + " entity Check");
        if (entity == null) {
            throw new IllegalArgumentException(baseName + " Entity cannot be null");
        }
    }

    protected void validateCreate(TCreateDTO dto) {
        log.info(baseName + " CreateDTO Check");
        if (dto == null) {
            throw new IllegalArgumentException(baseName + " CreateDTO cannot be null");
        }
    }

    protected void validateUpdate(TUpdateDTO dto) {
        log.info(baseName + " UpdateDTO Check");
        if (dto == null) {
            throw new IllegalArgumentException(baseName + " UpdateDTO cannot be null");
        }
    }

    /// -------------------------------------------
    ///         abstract이므로, 강제로 재정의 해야합니다.
    ///         DTO -> Entity 로 변환합니다.
    /// -------------------------------------------
    protected abstract TEntity createEntity(TCreateDTO dto);
    protected abstract TEntity updateEntity(TUpdateDTO dto, TEntity entity);
}
