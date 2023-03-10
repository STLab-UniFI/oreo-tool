# OREO: A Tool for Offline Run-time Monitoring and Fault-Error-Failure Chain Localization

This repository is a companion page for the following publication:
> Author Names. Publication year. Thesis / Paper title. Publication venue / proceedings.

It contains all the material required for replicating the study, including: 
installation steps, customization options and sample timelines extracted.

## How to cite us
The scientific article describing design, execution, and main results of this study is available [here](https://www.google.com).<br> 
If this study is helping your research, consider to cite it is as follows, thanks!

```
@article{,
  title={},
  author={},
  journal={},
  volume={},
  pages={},
  year={},
  publisher={}
}
```

## Quick start
In order to run the OREO tool alongside your JEE application, follow these steps:

### Getting started

1. Clone the repository 
   - `git clone https://github.com/...TBD`
2. Copy directories `beanTimelineManager` and the `timeLine`from `/src/main/java/` to the source directory of the application you want to observe with OREO. 
If the application is managed with maven the default path is  `/src/main/java/`.
3. Register OREO as CDI extension for your application
    - To do this, it is sufficient to copy the directory `services` from the `src/main/resources/META-INF/` path of this repository to the `src/main/resources/META-INF/` path of the target application.
4.Re-build the target application.
