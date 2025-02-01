package br.com.mvassoler.credentials.repositories;

import br.com.mvassoler.credentials.configurations.FunctionalBaseTest;
import br.com.mvassoler.credentials.configurations.OrderCustom;
import br.com.mvassoler.credentials.core.enums.TipoChave;
import br.com.mvassoler.credentials.core.utils.Utils;
import br.com.mvassoler.credentials.entities.SecretKey;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;

@TestMethodOrder(OrderCustom.class)
public class SecretKeyTest extends FunctionalBaseTest {

    @Autowired
    private SecretKeyRepository secretKeyRepository;
    @Autowired
    private Utils utils;

    @Test
    void T001_savaSecretKeyByCorporativoIsValid() {
        SecretKey secretKey = utils.generateKey(TipoChave.CORPORATIVO);
        SecretKey secretKeySaved = this.secretKeyRepository.save(secretKey);

        Assertions.assertNotNull(secretKeySaved.getId());
        Assertions.assertEquals(secretKey.getTipoChave(), secretKeySaved.getTipoChave());
    }

    @Test
    void T002_savaSecretKeyByPrivadoIsValid() {
        SecretKey secretKey = utils.generateKey(TipoChave.PRIVADO);
        SecretKey secretKeySaved = this.secretKeyRepository.save(secretKey);

        Assertions.assertNotNull(secretKeySaved.getId());
        Assertions.assertEquals(secretKey.getTipoChave(), secretKeySaved.getTipoChave());
    }

    @Test
    void T003_savaSecretKeyByPublicoIsValid() {
        SecretKey secretKey = utils.generateKey(TipoChave.PUBLICO);
        SecretKey secretKeySaved = this.secretKeyRepository.save(secretKey);

        Assertions.assertNotNull(secretKeySaved.getId());
        Assertions.assertEquals(secretKey.getTipoChave(), secretKeySaved.getTipoChave());
    }


}
