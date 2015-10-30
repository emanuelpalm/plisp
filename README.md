# PLISP - Palm's Lisp

A rudimentary Java LISP implementation, based on the language description in the paper ["The Roots of Lisp"][1] by Paul
Graham. The interpreter was written as a personal exercise to better understand interpreter and compiler construction.
Also, LISP has been a subject of personal interest for quite some time, and this project gave an excuse to investigate
the topic.

The implemented language only defines two significant constructs, atoms and conses. An atom is, in essence, a string
identifier that has no other significance than being an identifier. A cons is a pair of memory cells, CAR and CDR,
which may be used to refer to other atoms or conses. A valid program only contains a single expression.
 
Example:
```lisp
(
    (label subst (lambda (x y z)
        (cond
            ((atom z) (cond
                ((eq z y) x)
                ('t z)
            ))
            ('t (cons
                (subst x y (car z))
                (subst x y (cdr z))
            ))
        )
    ))
    (
        (label cadr (lambda (x) (car (cdr x))))
        (cadr
            (subst 'hello_world '2 '(1 2 3))
        )
    )
)
```

[1]: http://www.paulgraham.com/rootsoflisp.html

# Installation

Clone the repository to a local directory of your choice.

```sh
$ git clone https://github.com:emanuelpalm/plisp.git
```

Use gradle to compile the project into a JAR and generate a default start-up script.

Windows:
```bat
> gradlew.bat installApp
```

Other:
```sh
$ ./gradlew installApp
```

The build results of interest will be in the `build/install/plisp` directory, relative to the project root.

# Basic Usage

Assuming the project has been installed, the current directory is the project root, and the used machine is running a
unix-like system, the following command may be used to run the bundled example application:

```sh
$ ./build/install/plisp/bin/plisp example.plp
```
