package com.bolsadeideas.springboot.form.app.controllers;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
// import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.bolsadeideas.springboot.form.app.editors.NombreMayusculaEditor;
import com.bolsadeideas.springboot.form.app.editors.PaisPropertyEditor;
import com.bolsadeideas.springboot.form.app.editors.RoleEditor;
import com.bolsadeideas.springboot.form.app.models.domain.Pais;
import com.bolsadeideas.springboot.form.app.models.domain.Role;
import com.bolsadeideas.springboot.form.app.models.domain.Usuario;
import com.bolsadeideas.springboot.form.app.services.PaisService;
import com.bolsadeideas.springboot.form.app.services.RoleService;
import com.bolsadeideas.springboot.form.app.validation.UsuarioValidator;

@Controller
@SessionAttributes("usuario")
public class FormController {
	
	@Autowired
	private UsuarioValidator validator;
	
	@Autowired
	private PaisService paisService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private PaisPropertyEditor paisEditor;
	
	@Autowired
	private RoleEditor roleEditor;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(validator);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		// setLinient en false para que la correcion de fecha sea estricta
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, "fechaNacimiento", new CustomDateEditor(dateFormat, false));
		binder.registerCustomEditor(String.class,"nombre", new NombreMayusculaEditor());
		binder.registerCustomEditor(String.class,"apellido", new NombreMayusculaEditor());
		
		binder.registerCustomEditor(Pais.class, "pais" ,paisEditor);
		binder.registerCustomEditor(Role.class, "roles" ,roleEditor);
	}
	
	@ModelAttribute("genero")
	public List<String> genero() {
		return Arrays.asList("Hombre","Mujer");
	}
	
	@ModelAttribute("listaRoles")
	public List<Role> listadoRoles(){
		return this.roleService.listar();
	}
	
	@ModelAttribute("listaRolesString")
	public List<String> listaRolesString(){
		return Arrays.asList("ROLE_ADMIN","ROLE_USER","ROLE_MODERATOR");
	}
	
	@ModelAttribute("listaRolesMap")
	public Map<String,String> listaRolesMap(){
		Map<String,String> roles = new HashMap<String,String>();
		roles.put("ROLE_ADMIN", "Administrador");
		roles.put("ROLE_USER", "Usuario");
		roles.put("ROLE_MODERATOR", "Moderador");
		return roles;
	}
	
	@ModelAttribute("listaPaises")
	public List<Pais> listaPaises(){
		return paisService.listar();
	}
	
	@ModelAttribute("paises")
	public List<String> paises(){
		return Arrays.asList("España","Mexico","Chile","Argentina","Peru","Colombia","Venezuela");
	}

	@ModelAttribute("paisesMap")
	public Map<String,String> paisesMap(){
		Map<String,String> paises = new HashMap<String,String>();
		paises.put("ES", "España");
		paises.put("MX", "Mexico");
		paises.put("CL", "Chile");
		paises.put("AR", "Argentina");
		paises.put("PE", "Peru");
		paises.put("CO", "Colombia");
		paises.put("VE", "Venezuela");
		return paises;
	}
	
	
	@GetMapping("/form")
	public String form(Model model) {
		Usuario usuario = new Usuario();
		usuario.setNombre("Jhon");
		usuario.setApellido("Doe");
		usuario.setIdentificador("123.456.789-K");
		usuario.setHabilitar(true);
		usuario.setValorSecreto("Algun valor secreto *******");
		usuario.setPais(new Pais(3,"CL","Chile"));
		usuario.setRoles(Arrays.asList(new Role(2,"Usuario","ROLE_USER")));
		
		model.addAttribute("titulo","Formulario usuarios");
		model.addAttribute("usuario",usuario);
		return "form";
	}
	
	@PostMapping("/form")
	public String procesar(@Valid Usuario usuario, BindingResult result, Model model) {
		// validator.validate(usuario, result);
		
		if(result.hasErrors()) {
			/*Map<String, String> errores = new HashMap<>();
			result.getFieldErrors().forEach(err -> {
				errores.put(err.getField(), "El campo ".concat(err.getField()).concat(" ").concat(err.getDefaultMessage()));
			});
			model.addAttribute("error",errores);*/
			model.addAttribute("titulo","Resultado form");
			return "form";
		}
		
		// model.addAttribute("usuario", usuario);
		// model.addAttribute("password",password);
		// model.addAttribute("email", email);
		// eliminar la sesion 
		return "redirect:/ver";
	}
	
	@GetMapping("/ver")
	public String ver(@SessionAttribute(name="usuario",required = false) Usuario usuario, Model model,SessionStatus status) {
		if(usuario == null) {
			return "redirect:/form";
		}
		model.addAttribute("titulo","Resultado form");

		status.setComplete();
		return "resultado";		
	}
	
	@GetMapping({"","/","/index"})
	public String first(Model model) {
		model.addAttribute("titulo","Hola  Mundo");
		return "index";
	}
}
