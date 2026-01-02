//package com.social.meli.teste;
//
//import com.social.meli.dto.user.UserCreateDTO;
//import com.social.meli.service.UserService;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//@Component // Para o Spring detectar e rodar
//public class UserServiceTestRunner implements CommandLineRunner {
//
//    private final UserService userService;
//
//    // Injeção por construtor (padrão recomendado)
//    public UserServiceTestRunner(UserService userService) {
//        this.userService = userService;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        try {
//            // Monte seu DTO para criar usuário
//            UserCreateDTO dto = new UserCreateDTO();
//            dto.setEmail("test@example.com");
//            dto.setNickname("testuser");
//            dto.setPassword("123456");
//            dto.setPhone("99999-9999");
//            dto.setName("Nome de Teste");
//
//            userService.createUser(dto);
//            System.out.println("Usuário criado!");
//
//            // Tente criar outro igual para testar exceção
//            userService.createUser(dto);
//        } catch (Exception ex) {
//            System.out.println("Erro capturado no CommandLineRunner: " + ex.getMessage());
//        }
//    }
//}