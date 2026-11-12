package com.ms2transacoes.infrastructure;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public class UsuarioAutenticado
        implements UserDetails {

    private final UUID id;

    private final String nome;

    private final String email;

    public UsuarioAutenticado(
            UUID id,
            String nome,
            String email) {

        this.id = id;
        this.nome = nome;
        this.email = email;
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {

        return "";
    }

    @Override
    public Collection<? extends GrantedAuthority>
    getAuthorities() {

        return Collections.emptyList();
    }

    @Override
    public boolean isAccountNonExpired() {

        return true;
    }

    @Override
    public boolean isAccountNonLocked() {

        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {

        return true;
    }

    @Override
    public boolean isEnabled() {

        return true;
    }
}