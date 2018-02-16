
# Contribution Guidelines
For those wishing to contribute to this project once the doors open, 
the following simple guidlines can help to keep us all on the same page.

## Pull Requests and Commit Messages

Pull requests and commit messages should appear as follows: *`<identifier> <message text>`*

**Mx:** Milestone Number

**Sx:** Sprint Number

**Fx:** Feature followed by the feature number

**Bx:** Bug followed by the bug number

### Examples
*M2 Added user stories for parents*

*S1 B2006 added bounds checking to string input*

*S5 F1887 Added user feedback system to main application form*

**Please make sure to include some information about what you did in your pull request!** 

## Branch Names
Branch names should be the identifier followed by the bug and feature number and the team members first initial of their first name. These should all be lower case. Branch names should be in all lowercase with no spaces in the following format: `<identifier>-<team member first initial>`

The identifier for the branch name should be the same as the identifier for the pull request and commit message text but only using lower-case characters.

**mx:** Milestone Number

**sx:** Sprint Number

**fx:** Feature followed by the feature number

**bx:** Bug followed by the bug number

### Examples
*m1-fxxx-j*

*s1-bxxx-b*

## Git Workflow Example

When working with this git repository please remember to use the git forking workflow You can read more about this workflow [here](https://www.atlassian.com/git/tutorials/comparing-workflows/forking-workflow). 

All contributors should create a feature branch in their fork of the primary repository. Work locally on your feature branch until you have all the information you want in your feature branch. Push your feature branch to your fork. Once you are done with your feature branch you need to verify that your changes can be merged without conflict to the current code base. Update your current copy of the development branch. Merge the up-to-date development branch into your feature branch and resolve any merge conflicts. Once you have successfully merged the code from the upstream development branch into your feature branch create a pull request for your feature branch.

### Example Workflow
Getting started:
```bash
git checkout develop
git pull upstream develop
git checkout -b s1-f1-comment-a
``` 
Make your code changed working locally and when you have your feature your feature branch you can make sure your code merges nicely with the upstream development branch.

```bash
git checkout develop
git pull upstream develop
git push origin develop
git checkout s1-f1-a
git merge develop
```

Once you've resolved any merge conflicts you're ready to create a pull request from your new feature branch.

## Coding Conventions and Guidelines
### Style Guides
* Android kotlin style guide: https://android.github.io/kotlin-guides/style.html
* Android java style guide: Please attempt to adhere to code styles present currently wthin the project
* Android XML style guide: This should not be an issue as Androide Dev Studio does the xml for UI with a drag and drop
    development interface; however, for those industrious few please attempt to adhere to the google standard listed
    here: https://google.github.io/styleguide/xmlstyle.html

## Tools & Versions
<!--text here-->
