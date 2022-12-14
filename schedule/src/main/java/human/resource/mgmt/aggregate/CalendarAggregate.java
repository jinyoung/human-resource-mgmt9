package human.resource.mgmt.aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.*;

import human.resource.mgmt.command.*;
import human.resource.mgmt.event.*;
import human.resource.mgmt.query.*;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.ToString;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate
@Data
@ToString
public class CalendarAggregate {

    @AggregateIdentifier
    private String userId;

    private List<Event> events;

    public CalendarAggregate() {}

    @CommandHandler
    public void handle(AddCalendarCommand command) {
        ScheduleAddedEvent event = new ScheduleAddedEvent();
        BeanUtils.copyProperties(command, event);

        apply(event);
    }

    @CommandHandler
    public void handle(CancelCalendarCommand command) {
        ScheduleCanceledEvent event = new ScheduleCanceledEvent();
        BeanUtils.copyProperties(command, event);

        apply(event);
    }

    @CommandHandler
    public CalendarAggregate(RegisterCalendarCommand command) {
        CalendarRegisteredEvent event = new CalendarRegisteredEvent();
        BeanUtils.copyProperties(command, event);

        event.setUserId(createUUID());

        apply(event);
    }

    private String createUUID() {
        return UUID.randomUUID().toString();
    }

    @EventSourcingHandler
    public void on(ScheduleAddedEvent event) {}

    @EventSourcingHandler
    public void on(ScheduleCanceledEvent event) {}

    @EventSourcingHandler
    public void on(CalendarRegisteredEvent event) {
        BeanUtils.copyProperties(event, this);
    }
}
