package com.marcosmoreira.domainmodelstudio.bootstrap;

import com.marcosmoreira.domainmodelstudio.application.batchexport.ExportOpenProjectsForClientUseCase;
import com.marcosmoreira.domainmodelstudio.infrastructure.batchexport.FileSystemClientBatchExporter;
import com.marcosmoreira.domainmodelstudio.infrastructure.json.DmsProjectFileRepository;
import com.marcosmoreira.domainmodelstudio.application.catalog.DefaultDiagramTypeRegistry;
import com.marcosmoreira.domainmodelstudio.infrastructure.markdown.DiagramMarkdownImportDispatcher;
import com.marcosmoreira.domainmodelstudio.infrastructure.markdown.ConceptualModelMarkdownExporter;
import com.marcosmoreira.domainmodelstudio.infrastructure.markdown.DelegatingMarkdownDiagramExporter;
import com.marcosmoreira.domainmodelstudio.infrastructure.markdown.ModuleMapMarkdownExporter;
import com.marcosmoreira.domainmodelstudio.infrastructure.markdown.FreeGraphMarkdownExporter;
import com.marcosmoreira.domainmodelstudio.infrastructure.markdown.LogicalBusinessGraphMarkdownExporter;
import com.marcosmoreira.domainmodelstudio.infrastructure.markdown.RolesPermissionsMarkdownExporter;
import com.marcosmoreira.domainmodelstudio.infrastructure.markdown.FileSystemSourceMarkdownSynchronizer;
import com.marcosmoreira.domainmodelstudio.infrastructure.markdown.ScreenFlowMarkdownExporter;
import com.marcosmoreira.domainmodelstudio.infrastructure.markdown.WireframeMarkdownExporter;
import com.marcosmoreira.domainmodelstudio.infrastructure.markdown.UmlClassMarkdownExporter;
import com.marcosmoreira.domainmodelstudio.infrastructure.markdown.ArchitectureMarkdownExporter;
import com.marcosmoreira.domainmodelstudio.infrastructure.markdown.BehaviorMarkdownExporter;
import com.marcosmoreira.domainmodelstudio.infrastructure.markdown.logicalbusiness.LogicalBusinessMarkdownDiagramExporter;
import com.marcosmoreira.domainmodelstudio.infrastructure.markdown.batch.FileSystemMarkdownBatchReader;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.Map;
import com.marcosmoreira.domainmodelstudio.infrastructure.markdown.DataDictionaryMarkdownExporter;
import com.marcosmoreira.domainmodelstudio.infrastructure.pdf.DataDictionaryPdfExporter;
import com.marcosmoreira.domainmodelstudio.infrastructure.resources.ClasspathAiResourceCatalog;
import com.marcosmoreira.domainmodelstudio.infrastructure.resources.ClasspathAiResourceExporter;
import com.marcosmoreira.domainmodelstudio.infrastructure.resources.OfficialAiResourceDescriptors;
import com.marcosmoreira.domainmodelstudio.infrastructure.svg.MultiNotationSvgDiagramExporter;

/** Factory de adaptadores técnicos. */
public final class InfrastructureServicesFactory {

    public InfrastructureServices create() {
        ClasspathAiResourceCatalog aiResourceCatalog = new ClasspathAiResourceCatalog(OfficialAiResourceDescriptors.all());
        DmsProjectFileRepository projectRepository = new DmsProjectFileRepository();
        MultiNotationSvgDiagramExporter svgExporter = new MultiNotationSvgDiagramExporter();
        DelegatingMarkdownDiagramExporter markdownExporter = new DelegatingMarkdownDiagramExporter(Map.ofEntries(
                Map.entry(DiagramTypeId.CONCEPTUAL_MODEL, new ConceptualModelMarkdownExporter()),
                Map.entry(DiagramTypeId.ADMIN_MODULE_MAP, new ModuleMapMarkdownExporter()),
                Map.entry(DiagramTypeId.ROLES_PERMISSIONS_MAP, new RolesPermissionsMarkdownExporter()),
                Map.entry(DiagramTypeId.SCREEN_FLOW, new ScreenFlowMarkdownExporter()),
                Map.entry(DiagramTypeId.ADMIN_WIREFRAMES, new WireframeMarkdownExporter()),
                Map.entry(DiagramTypeId.UML_CLASS, new UmlClassMarkdownExporter()),
                Map.entry(DiagramTypeId.C4_CONTEXT, new ArchitectureMarkdownExporter()),
                Map.entry(DiagramTypeId.C4_CONTAINERS, new ArchitectureMarkdownExporter()),
                Map.entry(DiagramTypeId.TECHNICAL_DEPLOYMENT, new ArchitectureMarkdownExporter()),
                Map.entry(DiagramTypeId.BPMN_BASIC, new BehaviorMarkdownExporter()),
                Map.entry(DiagramTypeId.OPERATIONAL_FLOW, new BehaviorMarkdownExporter()),
                Map.entry(DiagramTypeId.UML_USE_CASE, new BehaviorMarkdownExporter()),
                Map.entry(DiagramTypeId.UML_ACTIVITY, new BehaviorMarkdownExporter()),
                Map.entry(DiagramTypeId.UML_SEQUENCE, new BehaviorMarkdownExporter()),
                Map.entry(DiagramTypeId.UML_STATE, new BehaviorMarkdownExporter()),
                Map.entry(DiagramTypeId.FREE_GRAPH, new FreeGraphMarkdownExporter()),
                Map.entry(DiagramTypeId.LOGICAL_BUSINESS_INTAKE, new LogicalBusinessMarkdownDiagramExporter()),
                Map.entry(DiagramTypeId.LOGICAL_BUSINESS_GRAPH, new LogicalBusinessGraphMarkdownExporter())));
        DataDictionaryPdfExporter dataDictionaryPdfExporter = new DataDictionaryPdfExporter();
        DataDictionaryMarkdownExporter dataDictionaryMarkdownExporter = new DataDictionaryMarkdownExporter();
        return new InfrastructureServices(
                new DiagramMarkdownImportDispatcher(new DefaultDiagramTypeRegistry()),
                new FileSystemMarkdownBatchReader(),
                projectRepository,
                new FileSystemSourceMarkdownSynchronizer(),
                svgExporter,
                markdownExporter,
                new ClasspathAiResourceExporter(aiResourceCatalog),
                dataDictionaryPdfExporter,
                dataDictionaryMarkdownExporter,
                new ExportOpenProjectsForClientUseCase(new FileSystemClientBatchExporter(
                        projectRepository,
                        svgExporter,
                        markdownExporter,
                        dataDictionaryMarkdownExporter,
                        dataDictionaryPdfExporter))
        );
    }

}
