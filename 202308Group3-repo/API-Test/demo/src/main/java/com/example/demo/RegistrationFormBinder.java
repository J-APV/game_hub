//package com.example.demo;
//
//import com.vaadin.flow.component.notification.Notification;
//import com.vaadin.flow.component.notification.NotificationVariant;
//import com.vaadin.flow.data.binder.BeanValidationBinder;
//import com.vaadin.flow.data.binder.ValidationException;
//import com.vaadin.flow.data.binder.ValidationResult;
//import com.vaadin.flow.data.binder.ValueContext;
//import org.springframework.beans.factory.annotation.Autowired;
//
//public class RegistrationFormBinder {
//
//    private SignUpForm registrationForm;
//
//    @Autowired
//    private UserService userService; // Declare the UserService field
//
//    // Pass the UserService instance
//    /**
//     * Flag for disabling first run for password validation
//     */
//    private boolean enablePasswordValidation;
//
//    public RegistrationFormBinder(SignUpForm registrationForm) {
//        this.registrationForm = registrationForm;
//    }
//
//    /**
//     * Method to add the data binding and validation logics
//     * to the registration form
//    */
//    public void addBindingAndValidation() {
//        BeanValidationBinder<UserDetails> binder = new BeanValidationBinder<>(UserDetails.class);
//        binder.bindInstanceFields(registrationForm);
//
//        // A custom validator for password fields
//        binder.forField(registrationForm.getPasswordField())
//                .withValidator(this::passwordValidator).bind("password");
//
//        // The second password field is not connected to the Binder, but we
//        // want the binder to re-check the password validator when the field
//        // value changes. The easiest way is just to do that manually.
//        registrationForm.getPasswordConfirmField().addValueChangeListener(e -> {
//            // The user has modified the second field, now we can validate and show errors.
//            // See passwordValidator() for how this flag is used.
//            enablePasswordValidation = true;
//
//            binder.validate();
//        });
//
//        // Set the label where bean-level error messages go
//        binder.setStatusLabel(registrationForm.getErrorMessageField());
//
//        // And finally the submit button
//        registrationForm.getSubmitButton().addClickListener(event -> {
//            try {
//                // Create empty bean to store the details into
//                UserDetails userBean = new UserDetails();
//
//                // Run validators and write the values to the bean
//                binder.writeBean(userBean);
//
//                // Typically, you would here call backend to store the bean
//                UserDetails user = new UserDetails();
//                user.setUserName(userBean.getUserName());
//                user.setPassword(userBean.getPassword());
//                user.setEmail(userBean.getEmail());
//
//                UserDetails savedUser = userService.createUser(user);
//                // Show success message if everything went well
//                showSuccess(userBean);
//            } catch (ValidationException exception) {
//                // validation errors are already visible for each field,
//                // and bean-level errors are shown in the status label.
//                // We could show additional messages here if we want, do logging, etc.
//            }
//        });
//    }
//
//    /**
//     * Method to validate that:
//     * <p>
//     * 1) Password is at least 8 characters long
//     * <p>
//     * 2) Values in both fields match each other
//     */
//    private ValidationResult passwordValidator(String pass1, ValueContext ctx) {
//
//        // Password length check
//
//        if (pass1 == null || pass1.length() < 8) {
//            return ValidationResult.error("Password should be at least 8 characters long");
//        }
//
//        if (!enablePasswordValidation) {
//            // user hasn't visited the field yet, so don't validate just yet, but next time.
//            enablePasswordValidation = true;
//            return ValidationResult.ok();
//        }
//
//        String pass2 = ctx.getValue();
//
//        if (pass1 != null && pass1.equals(pass2)) {
//            return ValidationResult.ok();
//        }
//
//        return ValidationResult.error("Passwords do not match");
//    }
//
//    /**
//     * We call this method when form submission has succeeded
//     */
//    private void showSuccess(UserDetails userBean) {
//        Notification notification =
//                Notification.show("Data saved, welcome " + userBean.getFirstName());
//        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
//
//        // Here you'd typically redirect the user to another view
//    }
//
//}