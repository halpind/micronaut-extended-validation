# micronaut-extended-validation

## What

`micronaut-validation` enables hibernate-validation for parameters received in controllers.

The validation is made by the ValidatingInterceptor which is enabled by the `@Validated` annotation. 

A limitation is, though, that validation groups is not (yet) supported. This project extends the  ValidatingInterceptor by 
reading validation groups from an annotation `UsingGroups`. THis will however require that the controller
uses the newly defined `@Validated` annotation

## Running

Build and run it through `Application`. The `PersonController` implements a very simple
controller where the new annotations are used.
