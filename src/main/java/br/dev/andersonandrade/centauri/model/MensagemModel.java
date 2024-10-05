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
        Usuario usuarioBanco = usuarioService.buscarPorCodigo(usuario.getCodigo());
        if (usuarioBanco.getMessagesSystem() != null || !usuarioBanco.getMessagesSystem().isEmpty()) {
            return new ArrayList<>(usuarioBanco.getMessagesSystem());
        } else {
            return List.of(new MensagemUsuario(null, "Você não tem mensagens!"));
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, noRollbackFor = Exception.class)
    public CaixaMensagem criaCaixaMensagem(Usuario usuario) {
        Usuario usuarioBanco = usuarioService.buscarPorCodigo(usuario.getCodigo());
        List<MensagemUsuario> mensagemSistema = usuarioBanco.getMessagesSystem()
                .stream()
                .filter(m -> !m.isLida()).toList();

        if (!mensagemSistema.isEmpty()) {
            return new CaixaMensagem(new ArrayList<>(mensagemSistema), usuario);
        }

        return new CaixaMensagem(List.of(new MensagemUsuario(null, "Você não tem mensagens!")), usuario);
    }
}
