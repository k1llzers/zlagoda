package org.naukma.zlagoda.security;

import lombok.RequiredArgsConstructor;
import org.naukma.zlagoda.employee.EmployeeRepository;
import org.naukma.zlagoda.exception.NoSuchEntityException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return employeeRepository.findByLogin(username)
                .orElseThrow(() -> new NoSuchEntityException("Can`t find employee by login: " + username));
    }
}
