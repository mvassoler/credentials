package br.com.mvassoler.credentials.core.configs;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

public class DatabaseLocaleResolver implements LocaleResolver {

    @NotNull
    @Override
    public Locale resolveLocale(@NotNull HttpServletRequest request) {
/*        Usuario usuarioAtual;
        try {
            usuarioAtual = Util.getUsuarioAtual();
        } catch (Exception e) {
            usuarioAtual = null;
        }

        if (usuarioAtual != null && usuarioAtual.getIdiomaPadrao() != null) {
            return Locale.forLanguageTag(usuarioAtual.getIdiomaPadrao().getCodigo().replace("_", "-"));
        }
        */
        //return Util.getLocaleDefault();
        return Locale.getDefault();
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        //Locale.setDefault(Util.getLocaleDefault());
        Locale.setDefault(Locale.getDefault());

    }


}