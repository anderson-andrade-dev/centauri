package br.dev.andersonandrade.centauri.configuracao;

import br.dev.andersonandrade.centauri.beans.DetalheUsuario;
import br.dev.andersonandrade.centauri.entity.Usuario;
import br.dev.andersonandrade.centauri.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DetalhesUsuarioService implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;

    public DetalhesUsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByLogin_Email(username).orElse(null);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuário não encontrado!");
        }
        return new DetalheUsuario(usuario);
    }

}
