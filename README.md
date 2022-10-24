# ArgoUML-SPL Evolved

*This data set will be published as part of the research paper "Preserving Consistency of Interrelated Models during View-Based Evolution of Variable Systems" at the 21st International Conference on Generative Programming: Concepts & Experiences (GPCE 2022).*

The ArgoUML-SPL Evolved comprises nine revisions of the ArgoUML-SPL case study, that were created by integrating the revisions of ArgoUML into the ArgoUML-SPL, starting with the commit of ArgoUML from which the ArgoUML-SPL was initially extracted (43d8b97e).  

For every commit of ArgoUML, the changes were manually  merged via [Meld](https://meldmerge.org/) into the ArgoUML-SPL. For every change in every file, the feature expressions were derived. Every change of a feature expression leads to a new system revision of the evolved ArgoUML-SPL. 

`ArgoUML-SPL-RevisionsLog.pdf` provides an overview of all nine revisions, the corresponding commit(s) of the original ArgoUML and the feature expression for every commit. 
All feature expressions (except for the core) were marked in yellow and every change that is visible in the ground-truth UML model in green. 

Note that minor non-intrusive changes were performed on multiple files to successfully parse the Java code using the Java Model Parser and Printer 1.4.1. For example, type imports of static fields or methods were added since otherwise Ecore proxy objects could not be resolved by JaMoPP. 
