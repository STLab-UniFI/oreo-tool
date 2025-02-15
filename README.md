# OREO: A Tool for Offline Run-time Monitoring and Fault-Error-Failure Chain Localization

This repository is a companion page for the following publication, submitted to the [Journal of Systems and Software](https://www.sciencedirect.com/journal/journal-of-systems-and-software):
> Leonardo Scommegna, Benedetta Picano, Roberto Verdecchia and Enrico Vicario. 2024. OREO: A Tool-Supported Approach for Offline Run-time Monitoring and Fault-Error-Failure Chain Localization. Journal of Systems and Software. (Currently under review).

It contains all the material required for running the OREO tool and generating the timeline of the application under observation, including: 
installation steps, customization options, and sample timelines extracted.

<!--
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
-->

## Quick start

OREO is a CDI extension that extracts runtime information and dynamically generates the timeline abstraction from the software architecture under observation.
In order to run the tool you will need an application of your choice that you want to observe.
In particular, OREO can be integrated with applications developed in Java and is able to detect lifecycles and interactions of components managed by CDI or EJB both from Java EE and Jakarta EE.

In order to run the OREO tool, follow these steps:

### Getting started

1. Clone the repository 
   - `git clone https://github.com/LeonardoScommegna/oreo-tool.git`

2. Copy the directories `beanTimelineManager` and `timeLine` from `/oreo_code/src/main/java/` of this project to the source directory of the application you want to observe with OREO. If the application is managed with maven the default path is  `/src/main/java/`. 
3. Add the following dependencies in the `pom.xml` of the target application:
```
<dependency>
  	<groupId>javax.enterprise</groupId>
	<artifactId>cdi-api</artifactId>
	<version>2.0.SP1</version>
	<scope>provided</scope>
</dependency>
 
<dependency>
	<groupId>org.jboss.weld</groupId>
	<artifactId>weld-core-impl</artifactId>
	<version>5.0.0.Final</version>
</dependency>
```
4. Register OREO as CDI extension for your application
    - To do this, it is sufficient to copy the directory `services` from the `oreo_code/src/main/resources/META-INF/` path of this repository to the `src/main/resources/META-INF/` path of the target application.
5. Re-build the target application.

#### Run OREO

1. Configure the filter. Namely [InstanceFilter.java](/oreo_code/src/main/java/beanTimelineManager/filter/InstanceFilter.java) 
    - In order to observe exclusively the instances of your interest, it is sufficient to update to the variable `PACKAGE_OF_INTEREST` inside the [InstanceFilter.java](/oreo_code/src/main/java/beanTimelineManager/filter/InstanceFilter.java) file to include the desired classes/packages.
2. Build the project and deploy on server
3. Use the application under observation normally
4. Once finished, the resulting timeline can be found in SERVER_ROOT/bin/log/

Sample Output Data
---------------
A sample of timeline output obtained using the [BooksComplete](https://github.com/Apress/Practical-JSF-Java-EE-8/tree/master/BooksComplete) open-source application is available [here](data/).

### Running OREO on Toy App

In order to provide an exemplary application, this repository includes Toy App, a simple web application.

You can find the source code at the path: `/toy_app_code/`.
An instrumented version of Toy App with docker is make available at path `/OREO_toy_app/`.

To launch Toy App with OREO, launch the following command from the `/OREO_toy_app/` directory:
```
docker compose up
```
Toy App will be usable from a browser at the address: `http://localhost:8080/simpleapp/`

After using the application, stop Docker (ctrl+c if run from the terminal), the resulting timeline in JSON format will be automatically saved in `/OREO_toy_app/data/`.

## Experiment Replication

To replicate the experiment, follow these steps:

#### Getting Started

1. Install the required Python packages:
   ```bash
   pip install -r requirements.txt
   ```

2. Navigate to the `scripts` directory:
   ```bash
   cd scripts
   ```

---

### Replicate Results of Step-Wise Failure Manifestation Analysis (Table 1)

Execute the following command from the `scripts` directory:
```bash
java -jar table1.jar
```

By default, it will analyze the `toyapp_tl.json` timeline located in `/scripts/data`. If you want to run the experiment with a different timeline, you can do so using the following command:

```bash
java -jar table1.jar "path/to/timeline"
```

---

### Replicate Results of Step-Wise Error Activation Analysis (Table 2)

Execute the following command from the `scripts` directory:
```bash
java -jar table2.jar
```

By default, it will analyze the `toyapp_tl.json` timeline located in `/scripts/data`. If you want to run the experiment with a different timeline, you can do so using the following command:

```bash
java -jar table2.jar "path/to/timeline"
```

---

### Replicate Results of Overhead Metrics (Table 3)

1. **Memory Overhead**  
   Run the following command:
   ```bash
   python3 memory.py
   ```

2. **Delay Overhead**  
   - For the Toy App:
     ```bash
     python3 prof.py toyapp-timeline.txt
     ```
   - For the Book App:
     ```bash
     python3 prof.py books-timeline.txt
     ```
---

### Notes:
- Ensure all files (`table1.jar`, `table2.jar`, `memory.py`, `prof.py`, and timeline files) are present in the `scripts` directory.
- The Empedocle timeline is not present because it is a closed-source project.
- To ensure the reproducibility of results, all provided scripts are based on the timelines extracted and used in the experimentation. However, the scripts work on any timeline extracted by OREO. Please follow the Quick Start section for more details.


## Repository Structure
This is the root directory of the repository. The directory is structured as follows:

    oreo-tool
     .
     |
     |--- oreo_code/                            Source code of OREO
     |
     |--- toy_app_code/                         Source code of Toy App
     |
     |--- OREO_toy_app/                         Source code of Toy App instrumented with OREO
     |
     |--- scripts/                         	Scripts to reproduce our results
     |
     |--- data/                                 Sample timelines
            |
            |--- output/                        Sample raw timelines generated by the OREO tool
            |
            |--- Figure/                        Sample timelines represented graphically












