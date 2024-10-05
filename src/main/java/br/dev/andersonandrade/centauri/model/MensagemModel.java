package br.dev.andersonandrade.centauri.model;

import br.dev.andersonandrade.centauri.beans.CaixaMensagem;
import br.dev.andersonandrade.centauri.entity.MensagemUsuario;
import br.dev.andersonandrade.centauri.entity.Usuario;
import br.dev.andersonandrade.centauri.interfaces.Mensagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class MensagemModel {

    private final UsuarioService usuarioService;


    @Autowired
    public MensagemModel(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public void enviar(Usuario usuario, String mensagem) {
        if (usuario != null) {
            MensagemUsuario mensagemSistema = new MensagemUsuario(usuario, mensagem);
            usuario.setMessagesSystem(List.of(mensagemSistema));
            usuarioService.salva(usuario);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, noRollbackFor = Exception.class)
    public List<Mensagem> lista(Usuario usuario) {
        Optional<Usuario> usuarioBanco = usuarioService.buscarPorCodigo(usuario.getCodigo());
        if (usuarioBanco.isPresent()) {
            if (usuarioBanco.get().getMessagesSystem() != null || !usuarioBanco.get().getMessagesSystem().isEmpty()) {
                return List.copyOf(usuarioBanco.get().getMessagesSystem());
            }
        }
            return List.of(new MensagemUsuario(null, "Você não tem mensagens!"));
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, noRollbackFor = Exception.class)
    public CaixaMensagem criaCaixaMensagem(Usuario usuario) {
        Optional<Usuario> usuarioBanco = usuarioService.buscarPorCodigo(usuario.getCodigo());

        if(usuarioBanco.isPresent()) {

            List<MensagemUsuario> mensagemSistema = usuarioBanco.get().getMessagesSystem();
                  mensagemSistema.stream()
                    .filter(m -> !m.isLida()).toList();

            if (!mensagemSistema.isEmpty()) {
                return new CaixaMensagem(List.copyOf(mensagemSistema), usuario);
            }
        }
        return new CaixaMensagem(List.of(new MensagemUsuario(null, "Você não tem mensagens!")), usuario);
    }
}
