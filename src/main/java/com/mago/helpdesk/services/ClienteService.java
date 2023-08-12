package com.mago.helpdesk.services;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.mago.helpdesk.domain.Pessoa;
import com.mago.helpdesk.domain.Cliente;
import com.mago.helpdesk.dtos.ClienteDTO;
import com.mago.helpdesk.repositories.PessoaRepository;
import com.mago.helpdesk.repositories.ClienteRepository;
import com.mago.helpdesk.services.exceptions.ObjectnotFoundException;

@Service
public class ClienteService {
	@Autowired
	private ClienteRepository repository;
	
	@Autowired
	private PessoaRepository pessoaRepository;

	
	public List<Cliente> findAll() {
		return repository.findAll();
	}
	public Cliente findById(Integer id) {
		Optional<Cliente> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectnotFoundException("Objeto não encontrado id: " + id));
	}
	

	public Cliente update(Integer id, @Valid ClienteDTO objDTO) {
		objDTO.setId(id);
		Cliente obj = findById(id);
		ValidaCpfEEmail(objDTO);
		obj = new Cliente(objDTO);
		return repository.save(obj);
	}
	public Cliente create(ClienteDTO objDTO) {
		objDTO.setId(null);
		ValidaCpfEEmail(objDTO);
		Cliente newObj = new Cliente(objDTO);
		return repository.save(newObj);
	}

	private void ValidaCpfEEmail(ClienteDTO objDTO) {
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
		Cliente obj = findById(id);
		if(obj.getChamados().size() > 0) {
			throw new DataIntegrityViolationException("Técnico possui ordens de serviços e não podem ser deletados! ");
		}
		repository.deleteById(id);
		
	}

}
