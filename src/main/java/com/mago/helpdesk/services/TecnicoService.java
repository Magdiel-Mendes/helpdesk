package com.mago.helpdesk.services;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.mago.helpdesk.domain.Pessoa;
import com.mago.helpdesk.domain.Tecnico;
import com.mago.helpdesk.dtos.TecnicoDTO;
import com.mago.helpdesk.repositories.PessoaRepository;
import com.mago.helpdesk.repositories.TecnicoRepository;
import com.mago.helpdesk.services.exceptions.ObjectnotFoundException;

@Service
public class TecnicoService {
	@Autowired
	private TecnicoRepository repository;
	
	@Autowired
	private PessoaRepository pessoaRepository;

	
	public List<Tecnico> findAll() {
		return repository.findAll();
	}
	public Tecnico findById(Integer id) {
		Optional<Tecnico> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectnotFoundException("Objeto não encontrado id: " + id));
	}
	

	public Tecnico update(Integer id, @Valid TecnicoDTO objDTO) {
		objDTO.setId(id);
		Tecnico obj = findById(id);
		ValidaCpfEEmail(objDTO);
		obj = new Tecnico(objDTO);
		return repository.save(obj);
	}
	public Tecnico create(TecnicoDTO objDTO) {
		objDTO.setId(null);
		ValidaCpfEEmail(objDTO);
		Tecnico newObj = new Tecnico(objDTO);
		return repository.save(newObj);
	}

	private void ValidaCpfEEmail(TecnicoDTO objDTO) {
		Optional<Pessoa> obj = pessoaRepository.findByCpf(objDTO.getCpf());
		if(obj.isPresent() && obj.get().getId() != objDTO.getId()) {
			throw new DataIntegrityViolationException("CPF já cadastrado no sistema! ");
		}
		obj = pessoaRepository.findByEmail(objDTO.getEmail());
		if(obj.isPresent() && obj.get().getId() != objDTO.getId()) {
			throw new DataIntegrityViolationException("Email já cadastrado no sistema! ");
		}
	}
	public void delete(Integer id) {
		Tecnico obj = findById(id);
		if(obj.getChamados().size() > 0) {
			throw new DataIntegrityViolationException("Técnico possui ordens de serviços e não podem ser deletados! ");
		}
		repository.deleteById(id);
		
	}

}
