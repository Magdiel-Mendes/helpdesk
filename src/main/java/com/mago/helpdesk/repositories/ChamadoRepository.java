package com.mago.helpdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mago.helpdesk.domain.Chamado;

public interface ChamadoRepository extends JpaRepository<Chamado, Integer>{

}
