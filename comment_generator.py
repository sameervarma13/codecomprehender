# comment_generator.py
import os
import asyncio
import aiohttp
from openai import AsyncOpenAI
from dotenv import load_dotenv
from concurrent.futures import ThreadPoolExecutor
import threading

# Load environment variables
load_dotenv()

# Initialize OpenAI client
client = AsyncOpenAI(api_key=os.getenv("OPENAI_API_KEY"))

# Thread-local storage for batch processing
_thread_local = threading.local()

def get_batch_requests():
    """Get or create batch requests list for current thread."""
    if not hasattr(_thread_local, 'batch_requests'):
        _thread_local.batch_requests = []
    return _thread_local.batch_requests


async def generate_method_summary_async(method_code: str) -> str:
    """Async version of method comment generation."""
    if not os.getenv("OPENAI_API_KEY"):
        raise Exception("OPENAI_API_KEY not found in environment variables")
    
    prompt = f"""
You are a senior Java developer writing JavaDoc-style comments for production code.

Your task is to generate a concise and professional JavaDoc comment for the given method. Follow these exact rules:

- Use `/** ... */` JavaDoc format.
- Write a one-line summary of the method's purpose using active voice.
- Use @param for each input with short, natural phrasing (e.g. "User's name")
- Use @return if the method returns a value (e.g. "List of users", "True if deleted")
- Avoid using articles like *the*, *a*, or *an* in parameter and return descriptions.
- Be clear and helpful, but never verbose.

### Desired Format Examples:

Example:
/**
 * Deletes a user by ID.
 *
 * @param id ID of the user
 * @return True if deleted
 */

Important:
- DO NOT include any markdown formatting like triple backticks (```).
- DO NOT include the word `java`.
- DO NOT wrap the comment in a code block.
- ONLY return the JavaDoc-style comment block using `/** ... */`.

Now write a comment for the following method:
{method_code}

Return ONLY the comment block.
"""
    
    try:
        response = await client.chat.completions.create(
            model="gpt-4o-mini",
            messages=[{"role": "user", "content": prompt}],
            temperature=0.2,
            max_tokens=500
        )
        return response.choices[0].message.content.strip()
    except Exception as e:
        raise Exception(f"OpenAI API error: {str(e)}")


async def generate_class_comment_async(class_code: str) -> str:
    """Async version of class comment generation."""
    if not os.getenv("OPENAI_API_KEY"):
        raise Exception("OPENAI_API_KEY not found in environment variables")
    
    prompt = f"""
You are a senior Java engineer. Write a concise JavaDoc-style comment block that describes what the given Java class does.

- Use `/** ... */` format
- 2–3 lines max describing the class purpose
- Use professional tone
- Return only the comment, no code, no markdown
- DO NOT include triple backticks or "java" keyword

Java class:
{class_code[:1000]}  # Truncate for API efficiency
"""
    
    try:
        response = await client.chat.completions.create(
            model="gpt-4o-mini",
            messages=[{"role": "user", "content": prompt}],
            temperature=0.2,
            max_tokens=300
        )
        comment = response.choices[0].message.content.strip()
        return comment.replace("```", "").strip()
    except Exception as e:
        raise Exception(f"OpenAI API error: {str(e)}")


async def batch_generate_comments(method_codes: list, class_code: str = None):
    """Generate comments for multiple methods and optionally a class in parallel."""
    tasks = []
    
    # Add class comment task if provided
    if class_code:
        tasks.append(generate_class_comment_async(class_code))
    
    # Add method comment tasks
    for method_code in method_codes:
        tasks.append(generate_method_summary_async(method_code))
    
    # Execute all tasks concurrently with rate limiting
    semaphore = asyncio.Semaphore(10)  # Limit to 10 concurrent requests
    
    async def limited_task(task):
        async with semaphore:
            return await task
    
    limited_tasks = [limited_task(task) for task in tasks]
    results = await asyncio.gather(*limited_tasks, return_exceptions=True)
    
    # Separate results
    class_comment = None
    method_comments = []
    
    if class_code:
        class_comment = results[0] if not isinstance(results[0], Exception) else None
        method_comments = results[1:]
    else:
        method_comments = results
    
    return class_comment, method_comments


# Synchronous wrapper functions for backward compatibility
def generate_method_summary(method_code: str) -> str:
    """Synchronous wrapper for async method comment generation."""
    return asyncio.run(generate_method_summary_async(method_code))


def generate_class_comment(class_code: str) -> str:
    """Synchronous wrapper for async class comment generation."""
    return asyncio.run(generate_class_comment_async(class_code))


def batch_generate_comments_sync(method_codes: list, class_code: str = None):
    """Synchronous wrapper for batch comment generation."""
    return asyncio.run(batch_generate_comments(method_codes, class_code))