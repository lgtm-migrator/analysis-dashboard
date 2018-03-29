package edu.hm.hafner.java.persistence;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.LineRange;
import edu.hm.hafner.analysis.LineRangeList;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Collectors.toList;

@Component
public class EntityMapper {

    private static final PropertyMap<Issue, IssueEntity> ISSUE_PROPERTY_MAP = new PropertyMap<Issue, IssueEntity>() {
        @Override
        protected void configure() {
            skip().setLineRanges(null);
        }
    };

    private static final PropertyMap<IssueEntity, IssueBuilder> ISSUE_ENTITY_PROPERTY_MAP = new PropertyMap<IssueEntity, IssueBuilder>() {
        @Override
        protected void configure() {
            skip().setLineRanges(null);
        }
    };

    private static final PropertyMap<Issues<Issue>, IssuesEntity> ISSUES_PROPERTY_MAP = new PropertyMap<Issues<Issue>, IssuesEntity>() {
        @Override
        protected void configure() {
            map().setErrorMessages(source.getErrorMessages().castToList());
            map().setInfoMessages(source.getInfoMessages().castToList());
            map().setSizeOfDuplicates(source.getDuplicatesSize());
            skip().setElements(null);
        }
    };

    private final ModelMapper mapper;

    public EntityMapper() {
        mapper = new ModelMapper();
        mapper.addMappings(ISSUE_PROPERTY_MAP);
        mapper.addMappings(ISSUE_ENTITY_PROPERTY_MAP);
        mapper.addMappings(ISSUES_PROPERTY_MAP);
        mapper.typeMap(IssuesEntity.class, Issues.class);
        mapper.typeMap(LineRange.class, LineRangeEntity.class);
        mapper.validate();
    }

    public IssueEntity map(final Issue issue) {
        return map(issue, new IssueEntity());
    }

    public IssueEntity map(final Issue issue, final IssueEntity entity) {
        getMapper().map(issue, entity);
        entity.setLineRanges(issue.getLineRanges().stream().map(lineRange -> getMapper().map(lineRange, LineRangeEntity.class)).collect(toList()));
        return entity;
    }

    public Issue map(final IssueEntity entity) {
        IssueBuilder builder = getMapper().map(entity, IssueBuilder.class);
        LineRangeList ranges = new LineRangeList();
        ranges.addAll(entity.getLineRanges().stream().map(lineRangeEntity -> new LineRange(lineRangeEntity.getStart(), lineRangeEntity.getEnd())).collect(toList()));
        builder.setLineRanges(ranges);

        Issue result = builder.build();
        setIssueId(result, entity.getId());
        return result;
    }

    public IssuesEntity map(final Issues<Issue> issues) {
        return map(issues, new IssuesEntity());
    }

    public IssuesEntity map(final Issues<Issue> issues, final IssuesEntity entity) {
        getMapper().map(issues, entity);
        Set<IssueEntity> issuesSet = ((Stream<Issue>)issues.stream()).map(this::map).collect(toSet());
        entity.setElements(issuesSet);
        return entity;
    }

    public Issues<Issue> map(final IssuesEntity entity) {
        Issues<Issue> result = getMapper().map(entity, Issues.class);
        entity.getInfoMessages().forEach(result::logInfo);
        entity.getErrorMessages().forEach(result::logError);
        entity.getElements().stream().map(this::map).forEach(result::add);
        return result;
    }

    private ModelMapper getMapper() {
        return mapper;
    }

    private void setIssueId(final Issue result, final UUID id) {
        Field idField = null;
        try {
            idField = result.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(result, id);
        }
        catch (NoSuchFieldException|IllegalAccessException exception) {
            throw new RuntimeException("The implementation of Issue has changed. The Mapper can't map Issue anymore.", exception);
        }
    }
}
