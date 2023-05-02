package com.generation.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.model.UsuarioLogin;
import com.generation.blogpessoal.repository.UsuarioRepository;
import com.generation.blogpessoal.service.UsuarioService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioControllerTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@BeforeAll
	void start() {

		usuarioRepository.deleteAll();

		usuarioService.cadastrarUsuario(new Usuario(0L, "Root", "root@root.com", "rootroot", " "));
	}

	@Test
	@DisplayName("Cadastrar um usuário")
	public void deveCriarUmUsuario() {

		HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(
				new Usuario(0L, "Paulo Antunes", "paulo_antunes@email.com.br", "13465278", "-"));

		ResponseEntity<Usuario> corpoResposta = testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST,
				corpoRequisicao, Usuario.class);

		assertEquals(HttpStatus.CREATED, corpoResposta.getStatusCode());
	}

	@Test
	@DisplayName("Não deve permitir duplicação do Usuário")
	public void nãoDeveDuplicarUsuario() {

		usuarioService.cadastrarUsuario(new Usuario(0L, "Maria da Silva", "maria_silva@email.com.br", "13465278", "-"));

		HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(new Usuario(0L, "Maria da Silva", "maria_silva@email.com.br", "13465278", "-"));

		ResponseEntity<Usuario> corpoResposta = testRestTemplate
				.exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, corpoResposta.getStatusCode());
	}

	@Test
	@DisplayName("Atualizar um Usuário")
	public void deveAtualizarUmUsuario() {

		Optional<Usuario> usuarioCadastrado = usuarioService.cadastrarUsuario(new Usuario(0L, "Juliana Andrews", "juliana_andrews@email.com.br", "juliana123", "-"));

		Usuario usuarioUpdate = new Usuario(usuarioCadastrado.get().getId(), "Juliana ","juliana@email.com.br", "juliana123", "-");

		HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(usuarioUpdate);

		ResponseEntity<Usuario> corpoResposta = testRestTemplate
				.withBasicAuth("root@root.com", "rootroot")
				.exchange("/usuarios/atualizar", HttpMethod.PUT, corpoRequisicao, Usuario.class);

		assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());

	}
	
	@Test
	@DisplayName("Deve Listar todos os Usuários")
	public void deveMostrarTodosUsuarios() {
		
		usuarioService.cadastrarUsuario(new Usuario(0L, "Sabrina Sanches", "sabrina_sanches@email.com.br", "sabrina123", "-"));
		
		usuarioService.cadastrarUsuario(new Usuario(0L, "Ricardo Marques", "ricardo_marques@email.com.br", "ricardo123", "-"));
		
		ResponseEntity<String> resposta = testRestTemplate
				.withBasicAuth("root@root.com", "rootroot")
				.exchange("/usuarios/all", HttpMethod.GET, null, String.class);
		
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
	}
	
	@Test
	@DisplayName("Deve procurar um usuário por id")
	public void deveProcurarPorId() {
		
		Optional<Usuario> usuarioCadastrado = usuarioService.cadastrarUsuario(new Usuario(0L, "João", "joao@email.com", "12345678", "-"));
		
		ResponseEntity<String> resposta = testRestTemplate
				.withBasicAuth("root@root.com", "rootroot")
				.exchange("/usuarios/" + usuarioCadastrado.get().getId(), HttpMethod.GET, null, String.class);
		
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
	}
	
	/*@Test
	@DisplayName("Deve fazer o Login de um usuário")
	public void deveFazerLogin() {
		
		usuarioService.cadastrarUsuario(new Usuario(0L, "Vitor", "vitor@email.com", "12345678", "-"));
		UsuarioLogin usuarioLogin = new UsuarioLogin("vitor@email.com", "12345678", "-");
		
		HttpEntity<Optional<UsuarioLogin>> corpoRequisicao = new HttpEntity<Optional<UsuarioLogin>>(Optional.of(usuarioLogin));
		ResponseEntity<UsuarioLogin> corpoResposta = testRestTemplate
				.exchange("/usuarios/logar", HttpMethod.POST, corpoRequisicao, UsuarioLogin.class);
		
		assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());
		
	}*/
}
