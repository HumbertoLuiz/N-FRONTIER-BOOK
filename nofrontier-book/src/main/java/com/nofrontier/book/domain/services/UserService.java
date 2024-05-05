package com.nofrontier.book.domain.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;

import com.nofrontier.book.api.v1.controller.UserRestController;
import com.nofrontier.book.config.PasswordEnconderConfig;
import com.nofrontier.book.core.modelmapper.ModelMapperConfig;
import com.nofrontier.book.core.publishers.NewUserPublisher;
import com.nofrontier.book.core.services.token.adapters.TokenService;
import com.nofrontier.book.core.validators.UserValidator;
import com.nofrontier.book.domain.exceptions.PasswordDoesntMatchException;
import com.nofrontier.book.domain.exceptions.RequiredObjectIsNullException;
import com.nofrontier.book.domain.exceptions.ResourceNotFoundException;
import com.nofrontier.book.domain.model.User;
import com.nofrontier.book.domain.repository.UserRepository;
import com.nofrontier.book.dto.v1.requests.UserRequest;
import com.nofrontier.book.dto.v1.responses.TokenResponse;
import com.nofrontier.book.dto.v1.responses.UserRegisterResponse;
import com.nofrontier.book.dto.v1.responses.UserResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

	private Logger logger = Logger.getLogger(UserService.class.getName());

	@Autowired
	PagedResourcesAssembler<UserResponse> assembler;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private PasswordEnconderConfig passwordEncoder;

	@Autowired
	private UserValidator validator;

	@Autowired
	private NewUserPublisher newUserPublisher;

	public PagedModel<EntityModel<UserResponse>> findAll(Pageable pageable) {
		logger.info("Finding all users!");
		var userPage = userRepository.findAll(pageable);
		var userDtoPage = userPage
				.map(u -> ModelMapperConfig.parseObject(u, UserResponse.class));
		userDtoPage.map(u -> u.add(
				linkTo(methodOn(UserRestController.class).findById(u.getKey()))
						.withSelfRel()));
		Link link = linkTo(methodOn(UserRestController.class).findAll(
				pageable.getPageNumber(), pageable.getPageSize(), "asc"))
				.withSelfRel();
		return assembler.toModel(userDtoPage, link);
	}

	// -------------------------------------------------------------------------------------------------------------

	public UserResponse findById(Long id) {
		logger.info("Finding one user!");
		var entity = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));
		var dto = ModelMapperConfig.parseObject(entity, UserResponse.class);
		dto.add(linkTo(methodOn(UserRestController.class).findById(id))
				.withSelfRel());
		return dto;
	}

	// -------------------------------------------------------------------------------------------------------------

	public UserResponse create(UserRequest request) {
		if (request == null)
			throw new RequiredObjectIsNullException();
		logger.info("Creating one person!");
		validatePasswordConfirmation(request);
		var userToSave = ModelMapperConfig.parseObject(request, User.class);
		validator.validate(userToSave);
		var passwordEncrypted = ((PasswordEncoder) passwordEncoder)
				.encode(userToSave.getPassword());
		userToSave.setPassword(passwordEncrypted);

		var userSaved = userRepository.save(userToSave);
		newUserPublisher.publish(userSaved);
		var response = ModelMapperConfig.parseObject(userSaved,
				UserResponse.class);
		response.add(linkTo(
				methodOn(UserRestController.class).findById(response.getKey()))
				.withSelfRel());
		var tokenResponse = generateTokenResponse(
				(UserRegisterResponse) response);
		((UserRegisterResponse) response).setToken(tokenResponse);
		return response;
	}

	// -------------------------------------------------------------------------------------------------------------

	public UserResponse update(UserResponse response) {
		if (response == null)
			throw new RequiredObjectIsNullException();
		logger.info("Updating a user!");
		var entity = userRepository.findById(response.getKey())
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));
		entity.setEmail(response.getEmail());
		entity.setCompleteName(response.getCompleteName());
		var dto = ModelMapperConfig.parseObject(userRepository.save(entity),
				UserResponse.class);
		dto.add(linkTo(
				methodOn(UserRestController.class).findById(dto.getKey()))
				.withSelfRel());
		return dto;
	}

	// -------------------------------------------------------------------------------------------------------------

	public void delete(Long id) {
		logger.info("Deleting one user!");
		var entity = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));
		userRepository.delete(entity);
	}

	// -------------------------------------------------------------------------------------------------------------

	private void validatePasswordConfirmation(UserRequest request) {
		var password = request.getPassword();
		var passwordConfirmation = request.getPasswordConfirmation();
		if (!password.equals(passwordConfirmation)) {
			var message = "the two password fields do not match";
			var fieldError = new FieldError(request.getClass().getName(),
					"passwordConfirmation", request.getPasswordConfirmation(),
					false, null, null, message);
			throw new PasswordDoesntMatchException(message, fieldError);
		}
	}

	private TokenResponse generateTokenResponse(UserRegisterResponse response) {
		var token = tokenService.generateAccessToken(response.getEmail());
		var refresh = tokenService.generateRefreshToken(response.getEmail());
		var tokenResponse = new TokenResponse(token, refresh);
		return tokenResponse;
	}

}