# Introduction

:+1::tada: First off, thanks for taking the time to contribute! :tada::+1:

The following is a set of guidelines for contributing to the ComposePreferences library. These are just guidelines, not rules. Use your best judgment, and feel free to propose
changes to this document in a pull request.

## Code of Conduct

The [Code of Conduct](CODE_OF_CONDUCT.md) governs this project and everyone participating in it. By participating, you are expected to uphold this code.
Please report unacceptable behavior to [info.dev.strable@gmail.com](mailto:info.dev.strable@gmail.com).

## How Can I Contribute?

### Reporting Bugs

To report a bug, open an issue on the [Issues](https://github.com/StrableD/ComposePreferences/issues/) page.
Please provide the following information:

- A clear and descriptive title
- A detailed description of the bug
- The operating system and the versions of the library and the dependencies
- Steps to reproduce the bug
- The expected behavior
- The actual behavior
- Screenshots, if possible

### Suggesting Enhancements Or New Features

To suggest an enhancement or a new feature, open an issue on the [Issues](https://github.com/StrableD/ComposePreferences/issues/) page.
Please provide the following information:

- A clear and descriptive title
- A detailed description of the enhancement or the new feature
- The expected behavior
- Possible solutions

You can also create a [pull request](#pull-requests) with the implementation of the enhancement or the new feature.

### Pull Requests

To contribute to the project, you can create a [pull request](https://github.com/StrableD/ComposePreferences/pull/new/master) with the changes you have made (read more
about [pull requests](http://help.github.com/pull-requests/)).
Please follow the [style guides](#style-guides) for your changes.
If possible, write tests for your changes. This can also be done by using the `app` module to test the changes to UI components.
When you create a new UI component, write a sample usage of the component in the `app` module and make shure it works as expected.

## Style Guides

### Git Commit Messages

- Always write a clear log message for your commits. One-line messages are fine for small changes, but bigger changes should include a summary and a short description of the
  change.

```git
$ git commit -m "A brief summary of the commit
>
> A paragraph describing what changed and its impact"
```

### Kotlin Style Guide

- Follow the [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Document your code, especially public functions, with [KDoc](https://kotlinlang.org/docs/kotlin-doc.html)
- For new preferences, you can use the existing preferences as a reference