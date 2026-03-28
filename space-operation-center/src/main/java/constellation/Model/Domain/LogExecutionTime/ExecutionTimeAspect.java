package constellation.Model.Domain.LogExecutionTime;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExecutionTimeAspect {

  @Around("@annotation(LogExecutionTime)")
  public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    long start = System.currentTimeMillis();

    Object proceed = joinPoint.proceed();

    long executionTime = System.currentTimeMillis() - start;
    System.out.println(joinPoint.getSignature() + " выполнено за " + executionTime + "мс");
    return proceed;
  }
}
